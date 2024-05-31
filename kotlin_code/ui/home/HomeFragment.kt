package com.trisnasejati.testingapps.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.trisnasejati.testingapps.BlogCreateActivity
import com.trisnasejati.testingapps.BlogReadActivity
import com.trisnasejati.testingapps.R
import com.trisnasejati.testingapps.api.adapter.BlogAdapter
import com.trisnasejati.testingapps.api.model.ApiFindBlog
import com.trisnasejati.testingapps.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var listBlog: RecyclerView
    private lateinit var progress: ProgressBar
    private lateinit var blogAdapter: BlogAdapter

    // menambahkan FloatingActionButton, jangan lupa tambahkan FloatingActionButton di layout
    private lateinit var btnAdd: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listBlog = view.findViewById(R.id.list_blog)
        progress = view.findViewById(R.id.progress)
        btnAdd = view.findViewById(R.id.btn_add) // coding baru

        listBlog.layoutManager = LinearLayoutManager(context)

        progress.setProgress(0, true)
        ApiFindBlog{result ->
            blogAdapter = BlogAdapter(result){ blog ->
                val intent = Intent(context, BlogReadActivity::class.java)
                intent.putExtra("id", blog.id)
                startActivity(intent)
            }
            listBlog.adapter = blogAdapter
            progress.setProgress(100, false)
        }.execute("https://crud.trisnawan.my.id/blog")

        // agar ketika di klik tampil BlogCreateActivity
        btnAdd.setOnClickListener {
            val intent = Intent(context, BlogCreateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}