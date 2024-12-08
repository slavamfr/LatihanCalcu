package com.example.latihancalcu

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.latihancalcu.R

class MainActivity : AppCompatActivity() {

    private lateinit var workingsTextView: TextView
    private lateinit var resultTextView: TextView
    private var workings = ""
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        workingsTextView = findViewById(R.id.txt_operation)
        resultTextView = findViewById(R.id.txt_result)

        setButtonListeners()
    }

    private fun setButtonListeners() {
        findViewById<TextView>(R.id.btn0).setOnClickListener { appendNumber("0") }
        findViewById<TextView>(R.id.btn1).setOnClickListener { appendNumber("1") }
        findViewById<TextView>(R.id.btn2).setOnClickListener { appendNumber("2") }
        findViewById<TextView>(R.id.btn3).setOnClickListener { appendNumber("3") }
        findViewById<TextView>(R.id.btn4).setOnClickListener { appendNumber("4") }
        findViewById<TextView>(R.id.btn5).setOnClickListener { appendNumber("5") }
        findViewById<TextView>(R.id.btn6).setOnClickListener { appendNumber("6") }
        findViewById<TextView>(R.id.btn7).setOnClickListener { appendNumber("7") }
        findViewById<TextView>(R.id.btn8).setOnClickListener { appendNumber("8") }
        findViewById<TextView>(R.id.btn9).setOnClickListener { appendNumber("9") }

        findViewById<TextView>(R.id.btnPlus).setOnClickListener { appendOperator("+") }
        findViewById<TextView>(R.id.btnMinus).setOnClickListener { appendOperator("-") }
        findViewById<TextView>(R.id.btnKali).setOnClickListener { appendOperator("*") }
        findViewById<TextView>(R.id.btnDivide).setOnClickListener { appendOperator("/") }
        findViewById<TextView>(R.id.btnPercent).setOnClickListener { appendOperator("%") }

        findViewById<TextView>(R.id.btnAC).setOnClickListener { clearAll() }
        findViewById<TextView>(R.id.btnSamadengan).setOnClickListener { calculateResult() }
        findViewById<TextView>(R.id.btnPoint).setOnClickListener { appendDot() }
    }

    private fun appendNumber(number: String) {
        workings += number
        workingsTextView.text = workings
    }

    private fun appendOperator(operator: String) {
        if (workings.isNotEmpty() && "+-*/%".contains(workings.last())) return
        workings += operator
        workingsTextView.text = workings
    }

    private fun appendDot() {
        if (workings.isNotEmpty() && !workings.endsWith(".")) {
            workings += "."
            workingsTextView.text = workings
        }
    }

    private fun clearAll() {
        workings = ""
        result = ""
        workingsTextView.text = workings
        resultTextView.text = result
    }

    private fun calculateResult() {
        try {
            val evalResult = eval(workings)
            result = evalResult.toString()
            resultTextView.text = result
        } catch (e: Exception) {
            resultTextView.text = "Error"
        }
    }

    // Function to evaluate mathematical expression
    private fun eval(expression: String): Double {
        return object {
            var pos = -1
            var ch: Char = ' '

            fun nextChar() {
                ch = if (++pos < expression.length) expression[pos] else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expression.length) throw RuntimeException("Unexpected: $ch")
                return x
            }

            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    x = when {
                        eat('+') -> x + parseTerm()
                        eat('-') -> x - parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    x = when {
                        eat('*') -> x * parseFactor()
                        eat('/') -> x / parseFactor()
                        eat('%') -> x % parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // unary plus
                if (eat('-')) return -parseFactor() // unary minus

                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parentheses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // numbers
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = expression.substring(startPos, this.pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: $ch")
                }
                return x
            }
        }.parse()
    }
}
