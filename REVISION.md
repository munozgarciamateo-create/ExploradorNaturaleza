# REVISION.md

Defectos y riesgos encontrados al revisar el código generado con IA, con su corrección.

## Checkpoint 3: capa de APIs (Retrofit, repositorio, ViewModel, pantallas)

### 1. Gson se salta la null-safety de Kotlin

- **Archivo:** `api/dto/ClimaDtos.kt`, `mapper/ApiMappers.kt`
- **Riesgo:** con un DTO no nulo como `val actual: ClimaActualDto`, si la API no envía `"current"` Gson deja el campo en `null` igual (usa reflexión). La app se cae con `NullPointerException` más adelante, en un lugar que no tiene nada que ver con la red, y el error no se muestra al usuario.
- **Corrección:** todos los campos de los DTO son nullable (`?`). El mapper `aClima()` valida los campos obligatorios y lanza `DatosInvalidosException`, que `llamadaSegura` convierte en `Resultado.Error` con un mensaje claro.

### 2. Orden de los `catch` y cancelación tratada como error

- **Archivo:** `repository/LlamadaSegura.kt`
- **Riesgo:** si `catch (e: IOException)` va antes que `catch (e: SocketTimeoutException)`, el timeout nunca muestra su mensaje (es hija de `IOException`). Y si se atrapa todo con `catch (e: Exception)`, también se atrapa la `CancellationException`: al tocar "Reintentar" se cancela la carga anterior y aparecería un error falso.
- **Corrección:** `catch` ordenados del más específico al más general, y `CancellationException` se relanza (`throw e`) antes que los demás.

### 3. "Reintentar" lanzaba cargas en paralelo

- **Archivo:** `ui/viewmodel/RazasViewModel.kt` (igual en `DetalleRazaViewModel`, `ClimaViewModel`, `GatosViewModel`)
- **Riesgo:** cada toque en "Reintentar" o "Actualizar" lanzaba una corrutina nueva sin cancelar la anterior. Si la primera respuesta llega después de la segunda, pisa el estado con datos viejos (condición de carrera) y gasta red de más.
- **Corrección:** se guarda el `Job` de la carga en curso y se cancela con `trabajo?.cancel()` antes de lanzar la nueva.

### 4. Filtro del buscador recalculado en cada recomposición

- **Archivo:** `ui/screens/RazasScreen.kt`
- **Riesgo:** `val filtradas = estado.razas.filter { ... }` directamente en el Composable recorre las ~100 razas en cada recomposición (por ejemplo, en cada cambio de estado que no tiene nada que ver con la búsqueda).
- **Corrección:** `remember(estado.razas) { derivedStateOf { ... } }`: la lista filtrada solo se recalcula cuando cambia el texto de búsqueda o la lista de razas.

## Checkpoint 4: capa de base de datos (Room)

### 5. Base de datos creada varias veces

- **Archivo:** `db/AppDatabase.kt`
- **Riesgo:** llamar `Room.databaseBuilder(...).build()` dentro de cada ViewModel abre varias conexiones al mismo archivo `explorador.db`: gasta memoria y los `Flow` de una instancia no se enteran de lo que escribe la otra (la lista de favoritas no se actualizaría).
- **Corrección:** singleton en el `companion object` con `@Volatile` y `synchronized`, y `AppDatabase.obtener(context)` en todos lados. Además se usa `context.applicationContext` para no retener una Activity.

### 6. Insertar dos veces la misma raza lanzaba error

- **Archivo:** `db/RazaFavoritaDao.kt`
- **Riesgo:** con `@Insert` sin estrategia, Room usa `ABORT`: si el usuario toca "Marcar favorita" dos veces rápido, la segunda inserción viola la clave primaria (`nombre`) y la app se cae con `SQLiteConstraintException`.
- **Corrección:** `@Insert(onConflict = OnConflictStrategy.REPLACE)`.

### 7. El botón de favorita no se actualizaba

- **Archivo:** `db/RazaFavoritaDao.kt`, `ui/viewmodel/DetalleRazaViewModel.kt`
- **Riesgo:** consultar "¿es favorita?" con una función `suspend` (lectura de una sola vez) deja el botón desactualizado: al quitarla desde otra pantalla o al guardarla, el texto del botón no cambia.
- **Corrección:** `esFavorita(nombre)` devuelve `Flow<Boolean>` y el ViewModel lo escucha en `init`; Room emite un valor nuevo cada vez que la tabla cambia.

### 8. Contexto de la Activity dentro del ViewModel

- **Archivo:** `ui/viewmodel/FavoritasViewModel.kt`, `ui/viewmodel/DetalleRazaViewModel.kt`
- **Riesgo:** pasar la Activity (o `LocalContext.current`) al ViewModel para crear la base de datos produce una fuga de memoria: el ViewModel sobrevive a la rotación y retiene una Activity destruida.
- **Corrección:** se usa `AndroidViewModel(application)`, cuyo contexto es el de la Application y vive lo mismo que la app.
