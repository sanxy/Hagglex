package com.sanxynet.hagglex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.sanxynet.hagglex.*
import com.sanxynet.hagglex.adapter.MainRecyclerView
import com.sanxynet.hagglex.adapter.Viewpager
import com.sanxynet.hagglex.databinding.FragmentDashBoardBinding
import com.sanxynet.hagglex.utils.*
import com.sanxynet.hagglex.viewmodel.MainViewModel
import java.lang.Math.abs


class DashBoardFragment : Fragment() {

    lateinit var userStatus : TextView
    lateinit var  amountdash : TextView
    lateinit var logout : View
    lateinit var switch: Switch
    private val viewModel: MainViewModel by activityViewModels()
    private  var _binding : FragmentDashBoardBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDashBoardBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userStatus = view.findViewById(R.id.dashboard_userId)
        val adapter = Viewpager()
        val pager : ViewPager2 = view.findViewById(R.id.pager)
        pager.adapter = adapter
        val dashRecyclerView : RecyclerView = view.findViewById(R.id.dashBoardRecyclerview)
        val reAdapter = MainRecyclerView()
        val layoutManager = LinearLayoutManager(requireContext())
        logout = view.findViewById(R.id.textbg)
        switch = view.findViewById(R.id.dashboard_switch)
        amountdash = view.findViewById(R.id.amount_value)



        layoutManager.orientation = LinearLayoutManager.VERTICAL
        dashRecyclerView.layoutManager = layoutManager
        dashRecyclerView.adapter = reAdapter

        pager.clipToPadding = false
        pager.clipChildren = false
        pager.offscreenPageLimit = 3
        pager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val marginPageTransformer = MarginPageTransformer(40)
        val compositePageTransformer = CompositePageTransformer()

        compositePageTransformer.addTransformer(marginPageTransformer)
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f

        }


        pager.setPageTransformer(compositePageTransformer)

        viewModel.userName.observe(viewLifecycleOwner,{
            userStatus.text = it.take(2).toUpperCase()
        })

        logout.setOnClickListener {
            generateMaterialDialog(
                requireActivity(), ALERT_TITLE, ALERT_MESSAGE,
                YES, NO, { signout() },{}
            )
        }

        switch.setOnCheckedChangeListener { p0, p1 ->
            if (p1) {
                amountdash.text = "NG567.00"

            } else {
                amountdash.text = "$1:00"
            }
        }


    }

    private fun signout() {

        requireActivity().finish()
        findNavController().navigate(R.id.logIn)
    }


}