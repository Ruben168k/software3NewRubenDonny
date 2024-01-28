import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserUtilsTest {

    @Test
    fun validUsername_returnsTrue() {
        assertTrue(UserUtils.isValidUsername("ValidUser123"))
    }

    @Test
    fun usernameTooShort_returnsFalse() {
        assertFalse(UserUtils.isValidUsername("Usr"))
    }

    @Test
    fun usernameContainsInvalidCharacter_returnsFalse() {
        assertFalse(UserUtils.isValidUsername("Invalid@Username"))
    }
}

object UserUtils {
    fun isValidUsername(username: String): Boolean {
        return username.length >= 5 && !username.contains("@")
    }
}

