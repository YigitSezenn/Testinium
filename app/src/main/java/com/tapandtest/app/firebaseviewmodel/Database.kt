import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseViewModel : ViewModel() {

    private val database = FirebaseDatabase.getInstance()
    private val tabloadi = database.getReference("Users")
    private val auth = FirebaseAuth.getInstance()
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    data class User(
        val userId: String? = null,
        val username: String? = null,
        val email: String? = null,
        val password: String? = null,
        val job: String? = null,
        val bio: String? = null,
        val profilePictureUrl: String ? = null,
        // val createdAt: Long = 0L // Ekstra bir tarih değeri isterseniz
    ) {
        constructor() : this(
            userId = "",
            username = "",
            email = "",
            password = "",
            job = "",
            bio = "",

        )

    }


    fun addUser(
        user: User
    ) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }
        tabloadi.child("users").child(userId).setValue(user)
            .addOnSuccessListener {
                println("Kullanıcı başarıyla eklendi!")
            }
            .addOnFailureListener { e ->
                println("Hata: ${e.message}")
            }
    }
    fun updateUser(

        user:User,
        callback: (Boolean) -> Unit
    )
    {
//        user.userId?.let {
//            userId ->
//            val UserRef = FirebaseDatabase.getInstance().getReference("Users").child("users").child(userId)
//
//            UserRef
//                .setValue(user).addOnSuccessListener {
//                    callback (true)
//                }
//                .addOnFailureListener { e ->
//                    println("Hata: ${e.message}")
//                    callback(false)
//                }
//        }

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        tabloadi.child("users").child(userId).setValue(user)
            .addOnSuccessListener {

                println("Kullanıcı başarıyla güncellendi!")
                callback(true)
            }
            .addOnFailureListener { e ->
                println("Hata: ${e.message}")
                callback(false)
            }
    }

    fun getUser(callback: (User?) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        if (userId == null) {
            println("Kullanıcı giriş yapmamış!")
            return
        }

        tabloadi.child("users").child(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    _user.value = user!!
                    callback(user) // Pass the user data to the callback
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Veri çekme iptal edildi: ${error.message}")
                }
            })
    }
    }





































//    fun getUser(callback: (List<User>) -> Unit) {
//        val auth = FirebaseAuth.getInstance()
//        val userId = auth.currentUser?.uid
//        if (userId == null) {
//            println("Kullanıcı giriş yapmamış!")
//            return
//        }
//
//// Giriş yapan kullanıcının kendi verilerini almak
//        tabloadi.child("users").child(userId)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val user = snapshot.getValue(User::class.java)
//                    if (user != null) {
//                        // Kullanıcının verisini burada işleyebilirsiniz
//                        callback(listOf(user))
//                    } else {
//                        println("Kullanıcı verisi alınamadı.")
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    println("Veri çekme iptal edildi: ${error.message}")
//                }
//            })
//    }
//}





//    fun addUser(user: User) {
//        val currentUserUid = auth.currentUser?.uid
//        if (currentUserUid == null) {
//            Log.e("DatabaseViewModel", "Kullanıcı giriş yapmamış!")
//            return
//        }
//
//        val newUserId = tabloadi.child(currentUserUid).push().key
//        if (newUserId != null) {
//            tabloadi.child(currentUserUid).child(newUserId).setValue(user)
//                .addOnSuccessListener {
//                    Log.d("DatabaseViewModel", "Kullanıcı başarıyla eklendi!")
//                }
//                .addOnFailureListener { e ->
//                    Log.e("DatabaseViewModel", "Hata: ${e.message}")
//                }
//        }
//    }
//}