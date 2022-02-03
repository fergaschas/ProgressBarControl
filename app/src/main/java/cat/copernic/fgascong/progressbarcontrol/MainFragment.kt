package cat.copernic.fgascong.progressbarcontrol

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import cat.copernic.fgascong.progressbarcontrol.MainViewModel.*
import cat.copernic.fgascong.progressbarcontrol.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        binding.apply {
            fragment = this@MainFragment
        }

        viewModel.progressBar1.observe(viewLifecycleOwner) { progress ->
            binding.progressBar1.progress = progress
        }

        viewModel.progressBar2.observe(viewLifecycleOwner) { progress ->
            binding.progressBar2.progress = progress
        }

        viewModel.jobState1.observe(viewLifecycleOwner){
            when(it){
                JobState.PREPARED -> setButtons1(start = true, stop = false, reset = false)
                JobState.RUNNING -> setButtons1(start = false, stop = true, reset = true)
                JobState.FINISHED -> setButtons1(start = false, stop = false, reset = true)
                JobState.CANCELLED -> setButtons1(start = true, stop = false, reset = true)
            }
        }

        viewModel.jobState2.observe(viewLifecycleOwner){
            when(it){
                JobState.PREPARED -> setButtons2(start = true, stop = false, reset = false)
                JobState.RUNNING -> setButtons2(start = false, stop = true, reset = true)
                JobState.FINISHED -> setButtons2(start = false, stop = false, reset = true)
                JobState.CANCELLED -> setButtons2(start = true, stop = false, reset = true)
            }
        }

        return binding.root
    }

    private fun setButtons1(start: Boolean, stop: Boolean, reset: Boolean) {
        binding.startProgressBar1.isEnabled = start
        binding.stopProgressBar1.isEnabled = stop
        binding.resetProgressBar1.isEnabled = reset
    }

    private fun setButtons2(start: Boolean, stop: Boolean, reset: Boolean) {
        binding.startProgressBar2.isEnabled = start
        binding.stopProgressBar2.isEnabled = stop
        binding.resetProgressBar2.isEnabled = reset
    }

    fun start(number: Int) {
        viewModel.start(number)
    }

    fun stop(number: Int) {
        viewModel.stop(number)
    }

    fun reset(number: Int) {
        viewModel.reset(number)
    }

}