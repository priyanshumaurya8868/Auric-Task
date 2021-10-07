package com.priyanshumaurya8868.aurictask.ui

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.priyanshumaurya8868.aurictask.R
import com.priyanshumaurya8868.aurictask.api.User
import com.priyanshumaurya8868.aurictask.databinding.ItemUserBinding
import com.priyanshumaurya8868.aurictask.databinding.UserFragmentBinding
import com.priyanshumaurya8868.aurictask.utils.*
import com.priyanshumaurya8868.aurictask.utils.Constants.KEY_USER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment<UserFragmentBinding>() {

    val viewModel : UserViewModel by viewModels()
    override fun getViewBinding() = UserFragmentBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe({lifecycle}){
            handleResources(view,it)
        }

    }

    private fun handleResources(view: View, res: Resource<List<User>>?) {
        when (res) {
            is Resource.Loading -> {
                binding.progressCircular.isVisible = true
            }
            is Resource.Error -> {
                binding.progressCircular.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getUsers()
                    }
                }.show()
            }
            is Resource.Success -> {
                setUpViews(res.data!!)
                binding.progressCircular.isVisible = false
            }
        }

    }

    private fun setUpViews(data: List<User>) {
        binding.rv.adapter = getAdapter(data)
    }

    private fun getAdapter(images: List<User>) = object : GenericListAdapter<User>(
        layoutId = R.layout.item_user,
        bind = { item, holder, itemCount ->
            val itemBinding =
                ItemUserBinding.bind(holder.itemView)
            itemBinding.apply {
                ivAvtar.load(item.avatar)
                tvDate.timeStamp = item.createdAt
                tvUsername.text = item.name
                root.setOnClickListener {
                    findNavController().navigate(
                        R.id.action_userFragment_to_transactionFragment,
                        bundleOf(Pair(KEY_USER, item))
                    )
                }
            }
        }
    ) {}.apply {
        submitList(images)
    }
}