package com.example.charades.data

/**
 * Representa una categoría de juego con su identificador, nombre y
 * lista de palabras posibles para adivinar.
 */
data class Category(
    val id: Int,
    val name: String,
    val words: List<String>
)

/**
 * Proveedor de datos en memoria para categorías y selección de palabras.
 */
object GameRepository {
    val categories = listOf(
        Category(
            id = 1,
            name = "Animales",
            words = listOf(
                "León", "Elefante", "Jirafa", "Tigre", "Oso", "Conejo", "Perro", "Gato",
                "Caballo", "Vaca", "Cerdo", "Gallina", "Pato", "Pez", "Ballena", "Delfín",
                "Pingüino", "Águila", "Búho", "Serpiente", "Cocodrilo", "Rana", "Mariposa",
                "Abeja", "Araña", "Cangrejo", "Pulpo", "Estrella de mar", "Tiburón", "Cebra"
            )
        ),
        Category(
            id = 2,
            name = "Películas",
            words = listOf(
                "Titanic", "Avatar", "Star Wars", "Harry Potter", "Jurassic Park", "E.T.",
                "Jaws", "Rocky", "Terminator", "Matrix", "Batman", "Superman", "Spiderman",
                "Iron Man", "Frozen", "Toy Story", "Shrek", "Cars", "Finding Nemo",
                "The Lion King", "Aladdin", "Beauty and the Beast", "Cinderella", "Snow White",
                "Pinocchio", "Dumbo", "Bambi", "Mickey Mouse", "Donald Duck", "Goofy"
            )
        ),
        Category(
            id = 3,
            name = "Profesiones",
            words = listOf(
                "Doctor", "Enfermero", "Profesor", "Bombero", "Policía", "Chef", "Piloto",
                "Ingeniero", "Arquitecto", "Abogado", "Juez", "Periodista", "Fotógrafo",
                "Artista", "Músico", "Actor", "Cantante", "Bailarín", "Deportista",
                "Veterinario", "Dentista", "Psicólogo", "Contador", "Vendedor", "Cajero",
                "Mesero", "Taxista", "Carpintero", "Electricista", "Plomero"
            )
        ),
        Category(
            id = 4,
            name = "Deportes",
            words = listOf(
                "Fútbol", "Baloncesto", "Tenis", "Voleibol", "Béisbol", "Golf", "Natación",
                "Ciclismo", "Atletismo", "Boxeo", "Karate", "Judo", "Esquí", "Surf",
                "Hockey", "Rugby", "Cricket", "Bádminton", "Ping Pong", "Bowling",
                "Arquería", "Tiro con arco", "Escalada", "Paracaidismo", "Buceo",
                "Patinaje", "Gimnasia", "Pesca", "Caza", "Correr"
            )
        ),
        Category(
            id = 5,
            name = "Comida",
            words = listOf(
                "Pizza", "Hamburguesa", "Tacos", "Sushi", "Pasta", "Arroz", "Pollo",
                "Carne", "Pescado", "Ensalada", "Sopa", "Sandwich", "Hot dog", "Papas fritas",
                "Helado", "Pastel", "Galletas", "Chocolate", "Manzana", "Plátano",
                "Naranja", "Fresa", "Uva", "Pera", "Durazno", "Cereza", "Kiwi",
                "Piña", "Mango", "Aguacate"
            )
        )
    )

    /**
     * Obtiene una palabra aleatoria perteneciente a la categoría indicada.
     * Devuelve null si la categoría no existe o no tiene palabras.
     */
    fun getRandomWord(categoryId: Int): String? {
        val category = categories.find { it.id == categoryId }
        return category?.words?.random()
    }

    /**
     * Recupera una categoría por su identificador, o null si no existe.
     */
    fun getCategoryById(id: Int): Category? {
        return categories.find { it.id == id }
    }
}
