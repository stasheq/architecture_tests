package me.szymanski.listtest.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.list_item.view.*
import me.szymanski.listtest.*
import me.szymanski.logic.cases.RepositoriesListCase
import me.szymanski.logic.rest.Repository

class MainFragment : BaseFragment<RepositoriesListCase>() {
    private val adapter = ReposAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        reposRecycler.adapter = this.adapter
        reposRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
    }

    override fun inject(component: ApplicationComponent) = component.inject(this)

    override fun linkCase(case: RepositoriesListCase) {
        case.loading.onNext { result -> reposSwipeRefresh.isRefreshing = result }
        case.list.onNext { result -> adapter.setItems(result) }
    }
}

class ReposAdapter : RecyclerView.Adapter<ReposAdapter.ViewHolder>() {
    private val repositories = ArrayList<Repository>()
    var onClick: ((id: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = repositories[position]
        holder.description.text = repo.description
        holder.title.text = repo.name
        holder.clickArea.setOnClickListener {
            onClick?.invoke(repo.id)
        }
    }

    override fun getItemCount() = repositories.size

    fun setItems(repositories: List<Repository>) {
        this.repositories.clear()
        this.repositories.addAll(repositories)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.itemTitle
        val description: TextView = view.itemDescription
        val clickArea = view.itemClickArea
    }
}
