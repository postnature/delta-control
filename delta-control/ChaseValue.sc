//(c) 2020 Erik Nyström, published under GNU General Public License, GPLv3.

ChaseValue {

	var <>minDelta=0.0001, <>maxDelta=1.0, minMult=0.0001, maxMult=1;

	var delta;

	*new { | minDelta=0.01, maxDelta=1, minMult=0.0, maxMult=1 |

		^super.newCopyArgs(minDelta, maxDelta, minMult, maxMult).init;

	}

	init {

		delta = DeltaList(1, minDelta, maxDelta);
		delta.addDelta;
	}
	//chase the value: the faster the changes, the closer to inVal the outVal will be.
	//negative curve gives steeper uphill chase slope and more jumpy behaviour
	chase { | inVal,curve=0 |

		var mult,outVal;

		mult=delta.addDelta[0];

		outVal=inVal*mult.lincurve(minDelta,maxDelta,maxMult,minMult,curve);

		^outVal
	}

	lastDelta {

		^delta.deltaList[0];
	}

}

// c=ChaseValue() initialise
// c.chase(100)//see if you can get a one by evaluating really fast.
