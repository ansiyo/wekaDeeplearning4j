/*
 * WekaDeeplearning4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WekaDeeplearning4j is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WekaDeeplearning4j.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Dl4jMlpFilter.java
 * Copyright (C) 2017-2018 University of Waikato, Hamilton, New Zealand
 */

package weka.filters.unsupervised.attribute;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import weka.classifiers.functions.Dl4jMlpClassifier;
import weka.core.Instances;
import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.OptionMetadata;
import weka.core.WekaException;
import weka.core.WekaPackageManager;
import weka.filters.Filter;
import weka.filters.SimpleBatchFilter;

/**
 * Weka filter that uses a neural network trained via {@link Dl4jMlpClassifier} as feature
 * transformation.
 *
 * @author Steven Lang
 */
public class Dl4jMlpFilter extends SimpleBatchFilter implements OptionHandler {

  private static final long serialVersionUID = 2125146603305868223L;

  /**
   * The classifier model this filter is based on.
   */
  protected File serializedModelFile = new File(WekaPackageManager.getPackageHome().toURI());

  protected Dl4jMlpClassifier model;

  /**
   * Layer index of the layer which is used to get the outputs from.
   */
  protected String transformationLayerName = "Output Layer";

  @OptionMetadata(
      description = "Layer name of the layer used for the feature transformation.",
      displayName = "layer name of the layer used for the feature transformation",
      commandLineParamName = "layerName",
      commandLineParamSynopsis = "-layerName <String>",
      displayOrder = 0
  )
  public String getTransformationLayerName() {
    return transformationLayerName;
  }

  public void setTransformationLayerName(String transformationLayerName) {
    this.transformationLayerName = transformationLayerName;
  }

  @OptionMetadata(
      description = "The trained Dl4jMlpClassifier object that contains the network.",
      displayName = "model used for feature transformation",
      commandLineParamName = "model",
      commandLineParamSynopsis = "-model <File>",
      displayOrder = 1
  )
  public File getSerializedModelFile() {
    return serializedModelFile;
  }

  public void setSerializedModelFile(File modelPath) {
    this.serializedModelFile = modelPath;
  }


  @Override
  public String globalInfo() {
    return null;
  }

  @Override
  public boolean allowAccessToFullInputFormat() {
    return true;
  }

  @Override
  protected Instances determineOutputFormat(Instances inputFormat) throws Exception {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(modelFile))) {
      model = (Dl4jMlpClassifier) ois.readObject();
      return model.getActivationsAtLayer(transformationLayerName, inputFormat);
    } catch (IOException e) {
      throw new WekaException("Could not read model file: " + modelFile.getAbsolutePath());
    }
  }


  @Override
  protected Instances process(Instances instances) throws Exception {
    return model.getActivationsAtLayer(transformationLayerName, instances);
  }


  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  public Enumeration<Option> listOptions() {

    return Option.listOptionsForClassHierarchy(this.getClass(), Filter.class).elements();
  }

  /**
   * Gets the current settings of the Classifier.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  public String[] getOptions() {

    return Option.getOptionsForHierarchy(this, Filter.class);
  }

  /**
   * Parses a given list of options.
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  public void setOptions(String[] options) throws Exception {

    Option.setOptionsForHierarchy(options, this, Filter.class);
  }
}
