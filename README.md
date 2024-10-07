- ¿Qué sucede si intentas borrar una encuesta que no existe? ¿Cómo lo has controlado?
No he probado a hacerlo.
  
- Si introduces en un texto del tipo <style>body background-color:red</style> en uno de los 
campos ¿Qué sucede al ver la encuesta? ¿el navegador ejecuta ese código o no? ¿porqué? 
¿cómo podrías hacer que se ejecute ese código o que se deje de ejecutar?
No he probado a hacerlo.

- ¿Qué has tenido que modificar en el repositorio para filtrar por motivo de búsqueda? ¿has 
tenido que escribir el código específico o como lo has realizado?
Más que modificar, he agregado un método para que busque mediante el repositorio por el nivel de satisfacción,
usando este método en el controlador para realizar el filtro. Luego en la vista de Thymeleaf, he hecho un
desplegable que llame al controlador que se encarga de realizar la búqueda por filtro.
