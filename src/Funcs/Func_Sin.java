package Funcs;

import Scanner.Func;

public class Func_Sin implements Func {

    @Override
    public double calculate(double num)
    {
        return Math.sin(num);
    }

}