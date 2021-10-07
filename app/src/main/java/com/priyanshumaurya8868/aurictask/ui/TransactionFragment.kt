package com.priyanshumaurya8868.aurictask.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.priyanshumaurya8868.aurictask.R
import com.priyanshumaurya8868.aurictask.api.Transaction
import com.priyanshumaurya8868.aurictask.api.User
import com.priyanshumaurya8868.aurictask.databinding.ItemTransactionBinding
import com.priyanshumaurya8868.aurictask.databinding.TransactionFragmentBinding
import com.priyanshumaurya8868.aurictask.utils.*
import com.priyanshumaurya8868.aurictask.utils.Constants.KEY_USER
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TransactionFragment : BaseFragment<TransactionFragmentBinding>() {

    val viewModel : TransactionViewModel by viewModels()
    override fun getViewBinding() = TransactionFragmentBinding.inflate(layoutInflater)

    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            user = it.getSerializable(KEY_USER) as User
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            getTransaction(user!!.id)
            transaction.observe({ lifecycle }) {
                handleResources(view, it)
            }
        }
    }

    private fun handleResources(view: View, res: Resource<List<Transaction>>) {
        when (res) {
            is Resource.Loading -> {
                binding.progressCircular.isVisible = true
            }
            is Resource.Error -> {
                binding.progressCircular.isVisible = false
                res.data?.let { setUpViews(it) }
                Snackbar.make(view, res.msg ?: "Something went Wrong", Snackbar.LENGTH_LONG).apply {
                    setAction("Retry") {
                        viewModel.getTransaction(user!!.id)
                    }
                }.show()
            }
            is Resource.Success -> {
                setUpViews(res.data!!)
                binding.progressCircular.isVisible = false
            }
        }

    }

    private fun setUpViews(data: List<Transaction>) {
        binding.apply {
            rv.adapter = getAdapter(data)
            ivAvtar.load(user!!.avatar)
            tvUsername.text = user!!.name
            ivBack.setOnClickListener {
              requireActivity().onBackPressed()
            }
        }

    }

    private fun getAdapter(images: List<Transaction>) = object : GenericListAdapter<Transaction>(
        layoutId = R.layout.item_transaction,
        bind = { item, holder, itemCount ->
            val itemBinding =
                ItemTransactionBinding.bind(holder.itemView)
            itemBinding.apply {
                tvCreatedAt.timeStamp = item.createdAt
                tvAmount.text = item.amount
                tvSymbol.text = item.symbol
                tvDesc.text = item.description
            }
        }
    ) {}.apply {
        submitList(images)
    }
}