package com.priyanshumaurya8868.aurictask.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyanshumaurya8868.aurictask.Repo
import com.priyanshumaurya8868.aurictask.api.Transaction
import com.priyanshumaurya8868.aurictask.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
val repo: Repo,
val cm: ConnectivityManager
) : ViewModel() {
    private var hasIntentConnection = false
    private val builder = NetworkRequest.Builder()
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            hasIntentConnection = true
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            hasIntentConnection = false
        }
    }

    init {
        cm.registerNetworkCallback(builder.build(), networkCallback)
    }

    private val _transaction = MutableLiveData<Resource<List<Transaction>>>()
    var transaction: LiveData<Resource<List<Transaction>>> = _transaction


    fun getTransaction(userId: Int) = viewModelScope.launch {
        _transaction.postValue(Resource.Loading())
        delay(100)
        try {
            if (hasIntentConnection) {
                val response = repo.getTransaction(userId)
                handleResources(response)
            } else _transaction.postValue(
                Resource.Error(
                    data = _transaction.value?.data,
                    msg = "No Internet Connection...!"
                )
            )
        } catch (e: Exception) {
            _transaction.postValue(
                Resource.Error(
                    data = _transaction.value?.data,
                    msg = e.message ?: "Something went Wrong"
                )
            )
        }
    }

    private fun handleResources(response: Response<List<Transaction>>) {
        if (response.isSuccessful) {
            _transaction.postValue(Resource.Success(response.body()!!))
        } else _transaction.postValue(
            Resource.Error(
                data = _transaction.value?.data,
                msg = response.message() ?: "Error ${response.code()} found...!"
            )
        )
    }


}