package com.capstone.petpoint.ui.myreport

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.petpoint.R
import com.capstone.petpoint.adapter.EmergencyUserAdapter
import com.capstone.petpoint.databinding.FragmentMyReportBinding
import com.capstone.petpoint.response.DataItem
import com.capstone.petpoint.ui.detail.DetailMyReportActivity

class MyReportFragment : Fragment() {

    private var _binding: FragmentMyReportBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MyReportAdapter
    private val myReportViewModel: MyReportViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MyReportAdapter { report ->
            val emergencyId = report.emId
            Log.i("MyReport Fragment", "Emergency id: $emergencyId")
            val intent = Intent(requireContext(), DetailMyReportActivity::class.java)
            intent.putExtra("EMERGENCY_ID", emergencyId)
            startActivity(intent)
        }

       binding.rvReportUser.apply {
           layoutManager = LinearLayoutManager(requireContext())
           adapter = this@MyReportFragment.adapter
       }
        myReportViewModel.setContext(requireContext())
        myReportViewModel.findEmergency()

        myReportViewModel.listEmergency.observe(viewLifecycleOwner) { reportList ->
            setReportData(reportList)
        }
    }

    private fun setReportData(reportList: List<DataItem>?) {
        adapter.submitList(reportList)
    }


}