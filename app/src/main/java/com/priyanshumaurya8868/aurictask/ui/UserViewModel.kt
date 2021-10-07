package com.priyanshumaurya8868.aurictask.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.aurictask.Repo
import com.priyanshumaurya8868.aurictask.api.User
import com.priyanshumaurya8868.aurictask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val repo: Repo,
    val cm: ConnectivityManager
) : ViewModel() {
    private var hasIntentConnection = false
    private var initiallyCalledUsers: Boolean = false
    private val _user = MutableLiveData<Resource<List<User>>?>()
    var user: LiveData<Resource<List<User>>?> = _user
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            hasIntentConnection = true
            if (_user.value?.data == null && initiallyCalledUsers) getUsers()
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            hasIntentConnection = false
        }
    }

    private val builder = NetworkRequest.Builder()
    init {
        cm.registerNetworkCallback(builder.build(), networkCallback)
        getUsers()
    }




    fun getUsers() = viewModelScope.launch {
        initiallyCalledUsers = true
        _user.postValue(Resource.Loading())
        delay(100)
        try {
            if (hasIntentConnection) {
                val response = repo.getUsers()
                handleResources(response)
            } else _user.postValue(
                Resource.Error(
                    data = _user.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _user.postValue(
                Resource.Error(
                    data = _user.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleResources(response: Response<List<User>>) {
        if (response.isSuccessful) {
            _user.postValue(Resource.Success(response.body()!!))
        } else _user.postValue(
            Resource.Error(
                data = _user.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }


}
