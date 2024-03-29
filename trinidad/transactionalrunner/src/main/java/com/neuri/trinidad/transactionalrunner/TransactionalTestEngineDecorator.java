package com.neuri.trinidad.transactionalrunner;

import fitnesse.trinidad.TestDescriptor;
import fitnesse.trinidad.TestEngine;
import fitnesse.trinidad.TestResult;


public class TransactionalTestEngineDecorator implements TestEngine{
	private TestEngine decoratedEngine;
	 public TransactionalTestEngineDecorator(String springContextPath, TestEngine underlyingEngine){
		FitnesseSpringContext.initialise(springContextPath); 
		this.decoratedEngine=underlyingEngine;
	}
	private TestResult testResult;
 	public TestResult runTest(final TestDescriptor test){
 	 		try {
			RollbackIntf rollback=FitnesseSpringContext.getRollbackBean();
			rollback.process(new Runnable(){
				public void run() {
					testResult=decoratedEngine.runTest(test);					
				}
			});
 			} catch (RollbackNow rn) {
 				System.err.println("rolling back now");
 			}
 			return testResult;
 	}
}
