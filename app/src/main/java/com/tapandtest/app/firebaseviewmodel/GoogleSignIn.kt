import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.tapandtest.app.R
import com.tapandtest.app.firebaseviewmodel.SignInResult
import com.tapandtest.app.firebaseviewmodel.UserData
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

@Suppress("DEPRECATION")
class GoogleAuthUiClient(
    private val context: Context, // Uygulamanın context'ini alıyoruz
    private val oneTapClient: SignInClient // Google One Tap oturum açma istemcisi
) {
    private val auth = Firebase.auth // Firebase Authentication nesnesi oluşturuluyor

    // Google ile giriş işlemini başlatan fonksiyon
    suspend fun signIn(): IntentSender? {
        val result = try {
            // Google kimlik doğrulama isteğini başlatıyoruz
            oneTapClient.beginSignIn(
                buildSignInRequest() // Giriş isteğini oluştur
            ).await() // Asenkron işlemi bekliyoruz
        } catch(e: Exception) {
            e.printStackTrace()
            // Eğer işlem iptal edilirse, istisnayı tekrar fırlat
            if(e is kotlinx.coroutines.CancellationException) throw e
            null // Hata oluşursa null döndür
        }
        return result?.pendingIntent?.intentSender // Giriş için IntentSender döndürülüyor
    }

    // Kullanıcının giriş işlemini tamamlamak için çağrılan fonksiyon
    suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent) // Giriş bilgilerini al
        val googleIdToken = credential.googleIdToken // Google ID Token'ını al
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null) // Google kimlik bilgilerini oluştur

        return try {
            // Firebase Authentication ile giriş yap
            val user = auth.signInWithCredential(googleCredentials).await().user
            // Giriş başarılı olursa kullanıcı bilgilerini döndür
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid, // Kullanıcının benzersiz kimliği
                        username = displayName, // Kullanıcı adı
                        profilePictureUrl = photoUrl?.toString() // Profil resmi URL'si
                    )
                },
                error = null // Hata yok
            )
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is kotlinx.coroutines.CancellationException) throw e
            // Giriş başarısız olursa hata mesajını döndür
            SignInResult(
                data = null,
                error = e.message
            )
        }
    }

    // Kullanıcının çıkış yapmasını sağlayan fonksiyon
    suspend fun signOut() {
        try {
            oneTapClient.signOut().await() // Google One Tap istemcisinden çıkış yap
            auth.signOut() // Firebase Authentication'dan çıkış yap
        } catch(e: Exception) {
            e.printStackTrace()
            if(e is kotlinx.coroutines.CancellationException) throw e
        }
    }

    // Şu an giriş yapmış olan kullanıcıyı döndüren fonksiyon
    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid, // Kullanıcı kimliği
            username = displayName, // Kullanıcı adı
            profilePictureUrl = photoUrl?.toString() // Profil fotoğrafı URL'si
        )
    }

    // Google giriş isteğini oluşturur
    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true) // Google ID Token'ı destekleniyor mu?
                    .setFilterByAuthorizedAccounts(false) // Daha önce yetkilendirilmiş hesaplarla sınırlama yapma
                    .setServerClientId(context.getString(R.string.default_web_client_id)) // Google API kimliği (Google Cloud'dan alınır)
                    .build()
            )
            .setAutoSelectEnabled(true) // Kullanıcı seçimi olmadan otomatik giriş yapmayı dene
            .build()
    }
}
