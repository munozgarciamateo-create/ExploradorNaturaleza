# IA_LOG.md

## Herramienta de IA usada

- **Claude** (Anthropic), modelo `claude-opus-5-5`, desde la app de Claude con acceso a la carpeta del proyecto.
- Se usó para: generar el diseño de clases y de la base de datos, el código de las capas (Retrofit, repositorios, ViewModels, pantallas, Room) y los commits.
- Todo commit generado o modificado con IA empieza con `[IA]`; los que escribí yo empiezan con `[Manual]`.

## Prompts principales

1. *"Este es un examen que permite el uso de IA. Ayúdame a realizar lo que se debe hacer en el grupo 2."* (adjunté el enunciado del examen y una foto del pizarrón con las capas API, Repository, Mapper y MVVM).
2. *(Contexto previo de estudio)* "Explícame capas de arquitectura empresarial, conexiones con APIs y MVVM, en qué elementos pertenece cada código", usando mi proyecto anterior Apis3 como ejemplo.
3. *(Contexto previo de estudio)* "Explícame Room."
4. Pedí que el proyecto reutilice la misma configuración de Gradle de Apis3 (AGP 9, Room con KSP, Retrofit, Navigation), porque esa ya compila en mi Android Studio.

## Decisiones del diseño propuesto por la IA que cambié y por qué

Al revisar el diseño se descartaron estas alternativas (la primera opción, más simple, frente a la decisión final):

| # | Alternativa descartada | Lo que quedó | Por qué |
| --- | --- | --- | --- |
| 1 | DTO con campos no nulos (`val actual: ClimaActualDto`) | Campos **nullable** + validación en el mapper | Gson ignora la null-safety de Kotlin: si la API no manda un campo, la app se caería con NullPointerException lejos del origen. |
| 2 | Detalle de la raza buscando dentro de la lista ya descargada (`list/all`) | Detalle con `GET breed/{raza}/list`, usando el nombre que llega como argumento de navegación | Así el detalle funciona aunque se abra directamente o se recree la pantalla, y practico `@Path`. |
| 3 | Guardar también la lista de subrazas en la tabla | Solo `nombre` (PK) y `cantidadSubrazas` | Es lo que pide el enunciado (`RazaFavoritaEntity`) y SQLite no guarda listas sin un TypeConverter. |
| 4 | Una sola instancia de Retrofit | **Una instancia por baseUrl** en `RetrofitClient` | Cada API tiene su propio dominio (dog.ceo, open-meteo.com, catfact.ninja); el enunciado pide instancias por baseUrl. |
| 5 | Nombres de paquetes genéricos (`data`, `domain`) | `api`, `repository`, `mapper`, `db`, `model`, `ui` | Para que coincida con las capas que explicó el docente en el pizarrón. |

### Cambios que hice yo después de revisar el código

- *(Completar: cualquier cambio que hagas tú al probar la app en el emulador, por ejemplo textos, colores o un error que corregiste. Esos commits van con `[Manual]`.)*
