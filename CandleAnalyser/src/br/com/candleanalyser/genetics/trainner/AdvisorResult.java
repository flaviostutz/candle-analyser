package br.com.candleanalyser.genetics.trainner;

import java.text.NumberFormat;

import br.com.candleanalyser.genetics.GeneticAdvisor;
import br.com.candleanalyser.simulation.OperationResult;

public class AdvisorResult implements Comparable<AdvisorResult> {

	private static final long serialVersionUID = 1L;
	
	private GeneticAdvisor advisor;
	private OperationResult operationResult;
	
	public AdvisorResult(GeneticAdvisor advisor, OperationResult operationResult) {
		this.advisor = advisor;
		this.operationResult = operationResult;
	}
	
	public GeneticAdvisor getAdvisor() {
		return advisor;
	}
	public OperationResult getOperationResult() {
		return operationResult;
	}

	@Override
	public int compareTo(AdvisorResult other) {
		if(operationResult==null) {
			return 1;
		} else if(other.operationResult==null) {
			return -1;
		} else {
			return operationResult.compareTo(other.operationResult);
		}
	}

	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		String str = "yield: undefined";
		if(operationResult!=null) str = "yield: " + nf.format(operationResult.getYield()*100) + "% ("+ advisor.getClass().getSimpleName() +")";
		return str;
	}
}
