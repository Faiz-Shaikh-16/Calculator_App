package com.example.calculator

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    var lastNumeric = false
    var stateError = false
    var lastDot = false

    private lateinit var expression: Expression


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun OnAllClearClick(view: View) {

        binding.dataTv.text = ""
        binding.resultTv.text = ""
        stateError=false
        lastDot=false
        lastNumeric=false
        binding.resultTv.visibility = View.GONE
    }

    fun OnClearClick(view: View) {

        binding.resultTv.text = ""
        lastNumeric= true
    }

    fun OnOperatorClick(view: View) {

        if(!stateError && lastNumeric){

            binding.dataTv.append((view as MaterialButton).text)
            lastDot=false
            lastNumeric=false
            onEqual()
        }
    }
    fun OnBackClick(view: View) {

        binding.dataTv.text = binding.dataTv.text.toString().dropLast(1)

        if(binding.dataTv.text.toString()==""){
            binding.resultTv.text = ""
        }
        try{
            val lastChar = binding.dataTv.text.toString()
            if(lastChar.isDigitsOnly()){
                onEqual()
            }

        }catch (e :  Exception){
            binding.resultTv.text = ""
            binding.resultTv.visibility = View.GONE
            Log.e("last char error",e.toString())
        }


    }
    fun OnDigitClick(view: View) {

        if(stateError){

            binding.dataTv.text = (view as MaterialButton).text
            stateError=false
        }
        else{
            binding.dataTv.append((view as MaterialButton).text)
        }
        lastNumeric = true
        onEqual()
    }
    fun OnEqualClick(view: View) {
        binding.resultTv.visibility = View.VISIBLE
        onEqual()

    }

    fun onEqual(){

        try {
            if (lastNumeric && !stateError) {

                val txt = binding.dataTv.text.toString()

                expression = ExpressionBuilder(txt).build()

                try {
                    var result = expression.evaluate()

                    binding.resultTv.visibility = View.VISIBLE

                    binding.resultTv.text = "=" + result.toString()
                } catch (e: ArithmeticException) {
                    Log.e("evaluate error", e.toString())
                    binding.resultTv.text = "Error"
                    stateError = true
                    lastNumeric = false

                }
            }
        }catch (e : Exception){
            Log.e("error",e.toString())
        }
    }
}