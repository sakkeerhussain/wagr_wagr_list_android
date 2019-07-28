package com.example.walklist.views.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import com.example.walklist.R
import com.example.walklist.controllers.WalkController
import com.example.walklist.views.activities.BaseActivity
import kotlinx.android.synthetic.main.dialog_create_walk.*

/*
 * Created by Sakkeer Hussain on 2019-07-28.
 */
class CreateWalkDialog(val activity: BaseActivity): Dialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_walk)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setListeners()
        // TODO - Populate a sample walk name such as 'Evening Walk', 'Morning Walk' etc...
    }

    private fun setListeners() {
        btCancel.setOnClickListener {
            dismiss()
        }

        btCreate.setOnClickListener {
            val title = etTitle.text.toString()
            if (title.isEmpty()) {
                etTitle.error = "Title required to create walk"
                return@setOnClickListener
            } else {
                etTitle.error = null
            }

            WalkController.createWalk(title, activity) {
                dismiss()
            }
        }
    }
}