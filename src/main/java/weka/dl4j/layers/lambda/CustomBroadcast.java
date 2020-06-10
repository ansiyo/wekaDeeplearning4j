package weka.dl4j.layers.lambda;

import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.api.ParamInitializer;
import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.FeedForwardLayer;
import org.deeplearning4j.nn.conf.layers.samediff.SameDiffLambdaLayer;
import org.deeplearning4j.nn.conf.memory.LayerMemoryReport;
import org.deeplearning4j.optimize.api.TrainingListener;
import org.nd4j.autodiff.samediff.SDVariable;
import org.nd4j.autodiff.samediff.SameDiff;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Broadcast;

import java.util.Collection;

public class CustomBroadcast extends SameDiffLambdaLayer {

    private long width;

    public CustomBroadcast() {}

    public CustomBroadcast(long width) {
        this.width = width;
    }

    @Override
    public SDVariable defineLayer(SameDiff sd, SDVariable x) {
        return x;
    }

    @Override
    public InputType getOutputType(int layerIndex, InputType inputType) {
        InputType.InputTypeConvolutional convolutional = (InputType.InputTypeConvolutional) inputType;
        long channels = convolutional.getChannels();
        return InputType.convolutional(width, width, channels, CNN2DFormat.NHWC);
    }
}
