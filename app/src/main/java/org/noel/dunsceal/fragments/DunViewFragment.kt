/*
 * Copyright (c) 2019 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  distribute, sublicense, create a derivative work, and/or sell copies of the
 *  Software in any work that is designed, intended, or marketed for pedagogical or
 *  instructional purposes related to programming, coding, application development,
 *  or information technology.  Permission for such use, copying, modification,
 *  merger, publication, distribution, sublicensing, creation of derivative works,
 *  or sale is expressly withheld.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.noel.dunsceal.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import org.noel.dunsceal.R
import org.noel.dunsceal.helpers.SnowFilter
import kotlinx.android.synthetic.main.fragment_hero.*
import kotlinx.coroutines.*
import org.noel.dunsceal.models.DunModel
import org.noel.dunsceal.models.DunViewModel
import java.net.URL

class DunViewFragment : Fragment() {

    private lateinit var dunViewModel: DunViewModel

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val DUN_KEY = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(dun: DunModel): DunViewFragment {
            return DunViewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DUN_KEY, dun)
                }
            }
        }
    }

    private val parentJob = Job()

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            //2
            coroutineScope.launch(Dispatchers.Main) {
                //3
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = getString(R.string.error_message)
            }

            GlobalScope.launch { println("Caught $throwable") }
        }

    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + parentJob + coroutineExceptionHandler)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dunViewModel = ViewModelProviders.of(this).get(DunViewModel::class.java).apply {
            setIndex(arguments?.getInt(DUN_KEY) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val tab = arguments?.getParcelable(DUN_KEY) as? DunModel
        val view = inflater.inflate(R.layout.fragment_hero, container, false)
        var textView: TextView = view.findViewById(R.id.tab_name)
        dunViewModel.name.observe(this, Observer<String> {
            textView.text = tab?.title
        })
        textView= view.findViewById(R.id.tab_desc)
        dunViewModel.desc.observe(this, Observer<String> {
            textView.text = tab?.description
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tab = arguments?.getParcelable(DUN_KEY) as? DunModel

        coroutineScope.launch(Dispatchers.Main) {
            val originalBitmap = getOriginalBitmapAsync(tab!!).await()
            //1
            val snowFilterBitmap = loadSnowFilterAsync(originalBitmap).await()
            //2
            loadImage(snowFilterBitmap)
        }
    }

    // 1
    private fun getOriginalBitmapAsync(dun: DunModel): Deferred<Bitmap> =
        // 2
        coroutineScope.async(Dispatchers.IO) {
            // 3
            URL(dun.url).openStream().use {
                return@async BitmapFactory.decodeStream(it)
            }
        }

    private fun loadSnowFilterAsync(originalBitmap: Bitmap): Deferred<Bitmap> =
        coroutineScope.async(Dispatchers.Default) {
            SnowFilter.applySnowEffect(originalBitmap)
        }

    private fun loadImage(snowFilterBitmap: Bitmap) {
        progressBar.visibility = View.GONE
        snowFilterImage?.setImageBitmap(snowFilterBitmap)
    }

}

private fun Bundle.putParcelable(dunKey: String, tabView: Int) {

}