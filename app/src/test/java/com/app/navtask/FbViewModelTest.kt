package com.app.navtask

import android.app.Activity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.app.navtask.ui.viewmodel.FbViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class FbViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var auth: FirebaseAuth

    private lateinit var viewModel: FbViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = FbViewModel(auth)
    }

    @Test
    fun `test signup success`() {
        val email = "test@example.com"
        val password = "password"

        val mockTask = FakeTask<AuthResult>()
        `when`(auth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)

        viewModel.onSignup(email, password)

        assert(viewModel.inProgress.value)

        val mockAuthResult = mock(AuthResult::class.java)
        mockTask.setResult(mockAuthResult)

        assert(!viewModel.inProgress.value)
        assert(viewModel.signedIn.value)
    }

    @Test
    fun `test signup failure`() {
        val email = "test@example.com"
        val password = "password"

        val mockTask = FakeTask<AuthResult>()
        `when`(auth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)

        viewModel.onSignup(email, password)

        assert(viewModel.inProgress.value)

        val mockAuthResult = mock(AuthResult::class.java)
        mockTask.setResult(mockAuthResult, false)

        assert(!viewModel.inProgress.value)
        assert(!viewModel.signedIn.value)
    }

    @Test
    fun `test login success`() {
        val email = "test@example.com"
        val password = "password"

        val mockTask = FakeTask<AuthResult>()
        `when`(auth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)

        viewModel.login(email, password)

        assert(viewModel.inProgress.value)

        val mockAuthResult = mock(AuthResult::class.java)
        mockTask.setResult(mockAuthResult)

        assert(!viewModel.inProgress.value)
        assert(viewModel.signedIn.value)
    }

    @Test
    fun `test login failure`() {
        val email = "test@example.com"
        val password = "password"

        val mockTask = FakeTask<AuthResult>()
        `when`(auth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)

        viewModel.login(email, password)

        assert(viewModel.inProgress.value)

        val mockAuthResult = mock(AuthResult::class.java)
        mockTask.setResult(mockAuthResult, false)

        assert(!viewModel.inProgress.value)
        assert(!viewModel.signedIn.value)
    }

    @Test
    fun `test getSignedInUser`() {
        val dummyUser = mock(FirebaseUser::class.java)
        `when`(auth.currentUser).thenReturn(dummyUser)

        val signedInUser = viewModel.getSignedInUser()

        assert(signedInUser == dummyUser)
    }

}


class FakeTask<T> : Task<T>() {
    private var result: T? = null
    private var exception: Exception? = null
    private var isSuccessful = false
    private var onCompleteListener: OnCompleteListener<T>? = null

    fun setResult(result: T, isSuccessful: Boolean = true) {
        this.result = result
        this.isSuccessful = isSuccessful
        onCompleteListener?.onComplete(this)
    }

    override fun isComplete(): Boolean {
        return true
    }

    override fun isSuccessful(): Boolean {
        return isSuccessful
    }

    override fun getResult(): T? {
        return result
    }

    override fun <X : Throwable?> getResult(p0: Class<X>): T {
        TODO("Not yet implemented")
    }

    override fun isCanceled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: OnFailureListener): Task<T> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: Activity, p1: OnFailureListener): Task<T> {
        TODO("Not yet implemented")
    }

    override fun addOnFailureListener(p0: Executor, p1: OnFailureListener): Task<T> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: Executor, p1: OnSuccessListener<in T>): Task<T> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: Activity, p1: OnSuccessListener<in T>): Task<T> {
        TODO("Not yet implemented")
    }

    override fun addOnSuccessListener(p0: OnSuccessListener<in T>): Task<T> {
        TODO("Not yet implemented")
    }

    override fun getException(): Exception? {
        return exception
    }

    override fun addOnCompleteListener(p0: OnCompleteListener<T>): Task<T> {
        onCompleteListener = p0
        return this
    }
}