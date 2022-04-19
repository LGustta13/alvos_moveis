

import org.ejml.data.DMatrixRMaj;
import org.ejml.simple.SimpleMatrix;

public class Reconciliation {

	private double[] reconciledFlowDouble;
	private SimpleMatrix reconciledFlow;
	private SimpleMatrix adjustment;
	private SimpleMatrix rawMeasurement;
	private SimpleMatrix standardDeviation;
	private SimpleMatrix varianceMatrix;
	private SimpleMatrix incidenceMatrix;
	private SimpleMatrix diagonalMatrix;
	private SimpleMatrix weightsArray;

	public Reconciliation(double[] _rawMeasurement, double[] _standardDeviation, double[][] _incidenceMatrix) {

		if ((_rawMeasurement != null) && (_standardDeviation != null) && (_incidenceMatrix != null)) {
			if ((_rawMeasurement.length == _standardDeviation.length)
					&& (_standardDeviation.length == _incidenceMatrix[0].length)) {
				this.incidenceMatrix = new SimpleMatrix(_incidenceMatrix);
				this.rawMeasurement = new SimpleMatrix(_rawMeasurement.length, 1, true, _rawMeasurement);
				this.standardDeviation = new SimpleMatrix(_standardDeviation.length, 1, true, _standardDeviation);
				double[] aux_StandardDeviation = _standardDeviation.clone();
				double[][] aux_varianceMatrix = new double[aux_StandardDeviation.length][aux_StandardDeviation.length];
				for (int i = 0; i < aux_varianceMatrix.length; i++) {
					for (int j = 0; j < aux_varianceMatrix[0].length; j++) {
						if (i == j) {
							aux_varianceMatrix[i][i] = aux_StandardDeviation[i];
						} else {
							aux_varianceMatrix[i][j] = 0;
						}
					}
				}

				this.varianceMatrix = new SimpleMatrix(aux_varianceMatrix);

				SimpleMatrix aux1 = this.varianceMatrix.mult(this.incidenceMatrix.transpose());
				aux1 = aux1.mult(this.incidenceMatrix.mult(aux1).invert());
				this.adjustment = aux1.mult(this.incidenceMatrix.mult(this.rawMeasurement));
				this.reconciledFlow = this.rawMeasurement.minus(this.adjustment);
				DMatrixRMaj temp = this.reconciledFlow.getMatrix();
				this.reconciledFlowDouble = temp.getData();

			} else {
				System.out.println(
						"the rawMeasurement and/or standardDeviation and/or incidenceMatrix have inconsistent data/size.");
			}

		} else {
			System.out.println("the rawMeasurement and/or standardDeviation and/or incidenceMatrix have null data.");
		}
	}

	public Reconciliation(double[] _rawMeasurement, double[] _standardDeviation, double[] _incidenceMatrix) {

		if ((_rawMeasurement != null) && (_standardDeviation != null) && (_incidenceMatrix != null)) {
			if ((_rawMeasurement.length == _standardDeviation.length)
					&& (_standardDeviation.length == _incidenceMatrix.length)) {

				this.incidenceMatrix = new SimpleMatrix(_incidenceMatrix.length, 1, true, _incidenceMatrix);
				this.rawMeasurement = new SimpleMatrix(_rawMeasurement.length, 1, true, _rawMeasurement);
				this.standardDeviation = new SimpleMatrix(_standardDeviation.length, 1, true, _standardDeviation);

				double[][] auxDiagonalMatrix = new double[_rawMeasurement.length + 1][_rawMeasurement.length + 1];
				double[] auxWeightsArray = new double[_rawMeasurement.length + 1];
				for (int i = 0; i < _rawMeasurement.length; i++) {
					double auxMP = Math.pow((_rawMeasurement[i] * _standardDeviation[i]), 2);
					auxDiagonalMatrix[i][i] = 2 / auxMP;
					auxWeightsArray[i] = (2 * _rawMeasurement[i]) / auxMP;
				}

				this.diagonalMatrix = new SimpleMatrix(auxDiagonalMatrix);
				this.diagonalMatrix.setColumn(auxDiagonalMatrix.length - 1, 0, _incidenceMatrix);
				this.diagonalMatrix.setRow(auxDiagonalMatrix.length - 1, 0, _incidenceMatrix);
				this.weightsArray = new SimpleMatrix(auxWeightsArray.length, 1, true, auxWeightsArray);

				this.reconciledFlow = this.diagonalMatrix.invert().mult(this.weightsArray);
				DMatrixRMaj temp = this.reconciledFlow.getMatrix();
				this.reconciledFlowDouble = temp.getData();

			} else {
				System.out.println(
						"the rawMeasurement and/or standardDeviation and/or incidenceMatrix have inconsistent data/size.");
			}

		} else {
			System.out.println("the rawMeasurement and/or standardDeviation and/or incidenceMatrix have null data.");
		}
	}

	public void printMatrix(double[][] _m) {

		if (_m != null) {
			for (int i = 0; i < _m.length; i++) {
				System.out.print("| ");
				for (int j = 0; j < _m[0].length; j++) {
					System.out.print(_m[i][j] + " ");
				}
				System.out.println("|");
			}
			System.out.println("");

		} else {
			System.out.println("the matrix has null data.");
		}
	}

	public void printMatrix(double[] _m) {

		if (_m != null) {
			for (int i = 0; i < _m.length; i++) {
				System.out.println("| " + _m[i] + " | ");
			}
			System.out.println("");

		} else {
			System.out.println("the array has null data.");
		}
	}

	public double[] getReconciledFlow() {
		return this.reconciledFlowDouble;
	}

	public SimpleMatrix getAdjustment() {
		return this.adjustment;
	}

	public SimpleMatrix getRawMeasurement() {
		return this.rawMeasurement;
	}

	public SimpleMatrix getStandardDeviation() {
		return this.standardDeviation;
	}

	public SimpleMatrix getVarianceMatrix() {
		return this.varianceMatrix;
	}

	public SimpleMatrix getIncidenceMatrix() {
		return this.incidenceMatrix;
	}

	public SimpleMatrix getDiagonalMatrix() {
		return this.diagonalMatrix;
	}

	public SimpleMatrix getWeightsArray() {
		return this.weightsArray;
	}
}