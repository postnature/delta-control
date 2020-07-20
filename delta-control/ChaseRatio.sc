//(c) 2020 Erik Nyström, published under GNU General Public License, GPLv3.

ChaseRatio {

	var <>ratio=1.1, <>minDelta=0.01, <>maxDelta=1, <>minOut=0.0, <>maxOut=127, <>boundary=\fold;
	var delta,prevVal=0, prevMult=1;

	* new { | ratio=1.1, minDelta=0.0001, maxDelta=1,  minOut=0.0, maxOut=127, boundary=\fold|

		^super.newCopyArgs(ratio, minDelta, maxDelta, minOut, maxOut, boundary).init;

	}

	init {

		delta=DeltaList(1,minDelta,maxDelta);
		delta.addDelta;

	}

	chase { | inVal, curve=0 |

	var dmult,multRatio;

	dmult=delta.addDelta[0];//delta


	dmult=dmult.lincurve(minDelta,maxDelta,1,0,curve);//shortest delta equals multiplier of 1, which is full ratio

	multRatio=((ratio-1)*dmult);
	multRatio=multRatio+1;
		"multratio: ".postf;multRatio.postln;
	//compare current inVal with previous inVal (prevVal) in order to determine whether to multiply or divide (up or down)

	if (inVal>prevVal, {prevMult=prevMult*multRatio});
	if (inVal<prevVal, {prevMult=prevMult/multRatio});

	switch(boundary,
			\clip, {prevMult=prevMult.clip(minOut,maxOut)},
			\fold, {prevMult=prevMult.fold(minOut,maxOut)},
			\wrap, {prevMult=prevMult.wrap(minOut,maxOut)},
		);
	//prevMult=prevMult.fold(minOut,maxOut);//make sure it's within boundaries

	prevVal=inVal; //set prevVal to current val for next iteration

	^prevMult;

	}

}

/*
(
MIDIClient.init;
 MIDIIn.connectAll;
 MIDIClient.destinations;

 c=ChaseRatio();

MIDIFunc.cc({|val|
	val=c.chase(val,-8);
	val.postln;
},0);
)

*/



