package com.sanxynet.hagglex.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sanxynet.hagglex.R
import com.sanxynet.hagglex.databinding.FragmentSetUpCompleteBinding


class SetUpCompleteFragment : Fragment() {

    private var _binding : FragmentSetUpCompleteBinding? = null
    private val binding
    get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSetUpCompleteBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exploreBtn.setOnClickListener {
            findNavController().navigate(R.id.action_setUpComplete_to_dashBoard)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}