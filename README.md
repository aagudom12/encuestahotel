- ¿Qué sucede si intentas borrar una encuesta que no existe? ¿Cómo lo has controlado?
Si intentas borrar una encuesta que no existe, la función findById, devolverá un optional.empty,
por lo que no habrá encontrado ninguna encuesta con el id y probablemente devuelva un error, pero lo he controlado,
de manera que si no está presente, redirija al listado de encuestas, y si sí lo está, esta encuesta, será eliminada.
  
- Si introduces en un texto del tipo <style>body background-color:red</style> en uno de los 
campos ¿Qué sucede al ver la encuesta? ¿el navegador ejecuta ese código o no? ¿porqué? 
¿cómo podrías hacer que se ejecute ese código o que se deje de ejecutar?
Sucede que si se ejecuta el cambio, porque el código está sanitizado.

- ¿Qué has tenido que modificar en el repositorio para filtrar por motivo de búsqueda? ¿has 
tenido que escribir el código específico o como lo has realizado?
Más que modificar, he agregado un método para que busque mediante el repositorio por el nivel de satisfacción,
usando este método en el controlador para realizar el filtro. Luego en la vista de Thymeleaf, he hecho un
desplegable que llame al controlador que se encarga de realizar la búqueda por filtro.
