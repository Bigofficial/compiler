package Funcs;

import Scanner.Func;

public class Func_Cos implements Func {

    @Override
    public double calculate(double num)
    {
        return Math.cos(num);
    }
}
