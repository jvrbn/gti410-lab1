package controller;

public class MyFilter extends Filter{

    private double filterMatrix[][] = null;


    public MyFilter(PaddingStrategy paddingStrategy,
                                ImageConversionStrategy conversionStrategy){

        super(paddingStrategy, conversionStrategy);
        filterMatrix = new double[3][3];

        filterMatrix[0][0] = filterMatrix[1][0] = filterMatrix[2][0] =
                filterMatrix[0][1] = filterMatrix[1][1] = filterMatrix[2][1] =
                        filterMatrix[0][2] = filterMatrix[1][2] = filterMatrix[2][2] = (1.0/9.0);
    }


}
