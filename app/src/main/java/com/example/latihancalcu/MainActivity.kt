package com.example.latihancalcu

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private lateinit var operationTextView: TextView
    private var input: String = ""
    private var operator: String = ""
    private var value1: Double = Double.NaN
    private var value2: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        resultTextView = findViewById(R.id.resultTextView)
        operationTextView = findViewById(R.id.operationTextView)

        // Set click listeners for number buttons
        setNumberClickListeners()

        // Set operator buttons
        setOperatorClickListeners()
    }

    private fun setNumberClickListeners() {
        val numberButtons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9
        )

        for (id in numberButtons) {
            findViewById<Button>(id).setOnClickListener {
                input += (it as Button).text.toString()
                updateOperationTextView()
            }
        }
    }

    private fun setOperatorClickListeners() {
        findViewById<Button>(R.id.btnAdd).setOnClickListener { setOperator("+") }
        findViewById<Button>(R.id.btnSubtract).setOnClickListener { setOperator("-") }
        findViewById<Button>(R.id.btnMultiply).setOnClickListener { setOperator("*") }
        findViewById<Button>(R.id.btnDivide).setOnClickListener { setOperator("/") }

        findViewById<Button>(R.id.btnEquals).setOnClickListener { calculateResult() }
        findViewById<Button>(R.id.btnClear).setOnClickListener { clearAll() }
    }

    private fun setOperator(op: String) {
        if (!value1.isNaN()) {
            calculateResult()
        } else {
            value1 = input.toDoubleOrNull() ?: Double.NaN
        }
        operator = op
        input = ""
        updateOperationTextView()
    }

    private fun calculateResult() {
        if (input.isNotEmpty()) {
            value2 = input.toDoubleOrNull() ?: 0.0
            value1 = when (operator) {
                "+" -> value1 + value2
                "-" -> value1 - value2
                "*" -> value1 * value2
                "/" -> if (value2 != 0.0) value1 / value2 else Double.NaN
                else -> value1
            }
            resultTextView.text = if (value1.isNaN()) "Error" else value1.toString()
            input = ""
            operator = ""
            operationTextView.text = ""
        }
    }

    private fun clearAll() {
        value1 = Double.NaN
        value2 = Double.NaN
        input = ""
        resultTextView.text = "0"
        operationTextView.text = ""
    }

    private fun updateOperationTextView() {
        // Update tampilan operasi di TextView atas
        operationTextView.text = if (operator.isNotEmpty()) {
            "$value1 $operator $input"
        } else {
            input
        }
    }
}
