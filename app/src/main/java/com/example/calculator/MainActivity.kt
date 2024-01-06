package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var checkAdd = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view:View){
        if(view is Button)
        {
            Workings.append(view.text)
            checkAdd = true
        }
    }

    fun operationAction(view:View){
        if(view is Button && checkAdd)
        {
            Workings.append(view.text)
            checkAdd = false
        }
    }

    fun negAction(view:View){}

    fun allClearAction(view:View){
        Workings.text = ""
        Results.text = ""
    }

    fun equalsAction(view:View){
        Results.text = calcResult()

    }
    private fun calcResult(): String
    {
        val digitsOP = digitsOp()
        if(digitsOP.isEmpty())
        {
            return ""
        }
        val timesDivide = timesDivideFunct(digitsOP)
        if(timesDivide.isEmpty())
        {
            return ""
        }

        val result = addSubtractFunct(timesDivide)

        return result.toString()
    }

    private fun addSubtractFunct(passedList: MutableList<Any>): Any {
        var result = passedList[0] as Float

        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val next = passedList[i+1] as Float
                if (operator == '+')
                {
                    result += next
                }
                if(operator == '-')
                {
                    result -= next
                }
            }
        }

        return  result
    }


    private fun timesDivideFunct(passedList: MutableList<Any>): MutableList<Any>
    {
        var list = passedList
        while (list.contains('X') || list.contains('/'))
        {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restart = passedList.size
        for(i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex && i < restart)
            {
                val operator = passedList[i]
                val prev = passedList[i-1] as Float
                val next = passedList[i+1] as Float
                when(operator)
                {
                    'X'->
                    {
                        newList.add(prev * next)
                        restart = i + 1
                    }
                    '/'->
                    {
                        newList.add(prev/next)
                        restart = i + 1
                    }
                    else->
                    {
                        newList.add(prev)
                        newList.add(operator)
                    }
                }
            }

            if(i > restart)
            {
                newList.add(passedList[i])
            }
        }
        return newList
    }

    private fun digitsOp(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var current = ""
        for(char in Workings.text)
        {
            if(char.isDigit())
            {
                current += char
            }
            else
            {
                list.add(current.toFloat())
                current = ""
                list.add(char)
            }
        }
        if(current != "")
        {
            list.add(current.toFloat())
        }
        return list
    }




}