Plataforma de Gestión de Eventos y Venta de Entradas
📝 Descripción del Proyecto
Este sistema es una solución integral para la exploración de eventos, selección de asientos y compra de entradas, permitiendo además la gestión administrativa y visualización de métricas de rendimiento

🧠 Pensamiento Computacional (RF-043)
¿Qué se solicita finalmente? Una plataforma para usuarios (compra de entradas) y administradores (gestión de catálogo y métricas)

¿Qué información es relevante? Datos de usuarios, eventos, recintos, zonas, asientos, compras e incidencias

¿Cómo se agrupa la información? Mediante un diagrama de clases con entidades de dominio y estructuras de soporte para patrones

¿Qué funcionalidades se solicitan? Registro, búsqueda de eventos, selección de asientos, gestión de carrito, pagos y reportes (CSV/PDF)

¿Cómo se distribuyen las funcionalidades? Segregadas por perfiles (Usuario/Admin) y siguiendo principios SOLID

¿Qué debo hacer para probar las funcionalidades? Inicializar el sistema con datos precargados y realizar flujos completos de venta

¿Qué puedo reutilizar? Librerías como Apache POI/PDFBox y patrones de diseño estándar

¿Cómo pruebo/escribo la solución en Java? Desarrollo en Java con JavaFX, aplicando SOLID y control de versiones con Git

🏗️ Patrones de Diseño Implementados
Patrones Creacionales (RF-049)
Singleton (Obligatorio): Garantiza una instancia única para la persistencia en memoria (clase Taquilla)

Factory Method: Centraliza la creación de diferentes tipos de usuarios (Admin, Cliente)

Builder: Permite la construcción paso a paso de objetos complejos como Compra

Patrones Estructurales (RF-050)
Decorator (Obligatorio): Añade servicios adicionales (seguros, VIP) a la compra de forma dinámica

Adapter: Unifica interfaces de pasarelas de pago externas (PayPal, Stripe)

Facade: Simplifica el flujo de compra orquestando múltiples servicios internos

Patrones de Comportamiento (RF-051)
Strategy (Obligatorio): Permite intercambiar algoritmos de pago en tiempo de ejecución

State: Gestiona el ciclo de vida de una compra (Creada, Pagada, Cancelada)

Observer: Notifica a los usuarios sobre cambios de estado en sus eventos o compras
.
🛠️ Tecnologías Utilizadas
Lenguaje: Java
Interfaz Gráfica: JavaFX (con JavaFX Charts para métricas)
.
Reportes: Apache POI / PDFBox
.
Control de Versiones: Git con estrategia de ramas activa
.
