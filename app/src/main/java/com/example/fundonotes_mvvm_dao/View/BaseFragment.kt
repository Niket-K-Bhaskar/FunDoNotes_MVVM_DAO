package com.example.fundonotes_mvvm_dao.View

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.fundonotes_mvvm_dao.Model.Utility.Utility

open class BaseFragment : Fragment() {
    private val utility = Utility()

    @RequiresApi(Build.VERSION_CODES.M)
    fun isInternetAvailable(): Boolean {
        return utility.isOnline(requireContext())
    }
}