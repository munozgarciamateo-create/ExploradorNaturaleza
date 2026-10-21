# Explorador de naturaleza y clima

App Android (Kotlin + Jetpack Compose) del Segundo Parcial Práctico, **Grupo 2**.

Consume tres APIs públicas con Retrofit y arquitectura MVVM:

| API | Endpoint | Pantalla |
| --- | --- | --- |
| Dog CEO | `https://dog.ceo/api/breeds/list/all` | Razas de perros (lista + detalle) |
| Open-Meteo | `https://api.open-meteo.com/v1/forecast?...` | Clima en La Paz |
| Cat Facts | `https://catfact.ninja/facts?limit=10` | Datos de gatos |

Persistencia local con Room: tabla `razas_favoritas` y pantalla "Razas favoritas".

Documentos del examen: [DISENO.md](DISENO.md) · [IA_LOG.md](IA_LOG.md) · [REVISION.md](REVISION.md)
