import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseRepository {
    private val db = FirebaseFirestore.getInstance()

    // Method to read data from Firebase Firestore
    suspend fun getItems(): List<Item> {
        return try {
            val snapshot = db.collection("items").get().await()
            snapshot.documents.mapNotNull { it.toObject(Item::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    // Method to add a new item to Firebase Firestore
    suspend fun addItem(item: Item) {
        try {
            db.collection("items").add(item).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
