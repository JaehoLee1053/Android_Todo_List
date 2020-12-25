package com.goodlucklee.todolist

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.goodlucklee.todolist.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_todo.*
import kotlinx.android.synthetic.main.item_todo.view.*

// ViewModel Scope
/*
* Activity Create
* onStart() --> onResume() -->
* */
/*
* Activity rotate
* onPause() --> onStop() --> onDestroy()
* --> onStart() --> onResume() --> finish()
* */
/*
* onPause() --> onStop() --> onDestroy()
* Finished
* */
// onCleared()
// 새로운 Activity가 켜져서 기존 데이터가 사라짐

class MainActivity : AppCompatActivity() {
    val RC_SIGN_IN = 1000;
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그인 안 됨
        if (FirebaseAuth.getInstance().currentUser == null) {
            login()
        }

        val adapter = TodoAdapter(
            todoList = viewModel.todoList,
            infalter = LayoutInflater.from(this@MainActivity),
            onClickDeleteIcon = {
                viewModel.deleteTodo(it)
            },
            onClickItem = {
                viewModel.toggleTodo(it)
            }
        )
        recycler_view.adapter = adapter

        add_button.setOnClickListener {
            val todo = Todo(edit_text.text.toString())
            viewModel.addTodo(todo)
            edit_text.text.clear()
            recycler_view.adapter?.notifyDataSetChanged()
        }

        viewModel.todoLiveData.observe(this@MainActivity, Observer {
            (recycler_view.adapter as TodoAdapter).setData(it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("ddddd", "requestCode: $requestCode")

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            Log.d("ddddd", "response: $response")
            Log.d("ddddd", "resultCode: $resultCode")
            Log.d("ddddd", "Activity.RESULT_OK: ${Activity.RESULT_OK}")

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                Log.d("ddddd", "Success")
                viewModel.fetchData()
            } else {
                // 로그인 실패
                Log.d("ddddd", "Fail")
                finish()
            }
        }
    }

    fun login() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    fun logout() {
        AuthUI.getInstance()
            .signOut(this@MainActivity)
            .addOnCompleteListener {
                login()
            }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main,  menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_log_out -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


