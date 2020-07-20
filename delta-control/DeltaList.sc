//(c) 2020 Erik Nyström, published under GNU General Public License, GPLv3.

DeltaList {

	var <>maxSize=nil,<>minDelta=0.01, <>maxDelta=1,ifFull=\shift,<deltaList,<lastTime;
	var <newDelta,countRoutine,deltaIndex, started=false;

* new { | maxSize=nil, minDelta=0.01,maxDelta=1,ifFull=\shift |

		^super.newCopyArgs(maxSize,minDelta,maxDelta,ifFull);
	}

addDelta {
		if (started==false, {
		countRoutine=Routine{ inf.do{ | i | i.yield } };
		deltaList=List.new;
		lastTime=Main.elapsedTime;
		started=true;
		}, {
			newDelta=(Main.elapsedTime-lastTime).clip(minDelta, maxDelta);
		lastTime=Main.elapsedTime;
		deltaIndex=countRoutine.next;
		if (maxSize != nil, {
			if (deltaList.size >= maxSize, {
			switch(ifFull,
				\wrap, { deltaList.wrapPut(deltaIndex,newDelta);},
				\shift, { deltaList=deltaList.asArray.shift(-1,newDelta);deltaList=deltaList.asList;}
			);
		}, {deltaList.add(newDelta); })},
		{ deltaList.add(newDelta); });
		});
		^deltaList;
	}

//insert into list and split current delta into two segments
 insertSplit { | index, timeAtIndex |
		newDelta=Main.elapsedTime-timeAtIndex;
		deltaList=deltaList.insert(index+1,(deltaList[index]-newDelta).clip(minDelta, maxDelta));
		deltaList.put(index,newDelta);
		if (maxSize != nil, { if ( deltaList.size >= maxSize, {deltaList.pop;})});
		^deltaList;
	}
//replace a delta with the old plus a new
 replaceAdd { | index, timeAtIndex |
		newDelta=(Main.elapsedTime-timeAtIndex).clip(minDelta, maxDelta);
		deltaList.put(index,deltaList[index]+newDelta);
		if (maxSize != nil, { if ( deltaList.size >= maxSize, {deltaList.pop;})});
		^deltaList;
	}
	//replace an index with a new one (don't add)
 replace { | index, timeAtIndex |
		newDelta=(Main.elapsedTime-timeAtIndex).clip(minDelta, maxDelta);
		deltaList.put(index,newDelta);
		if (maxSize != nil, { if ( deltaList.size >= maxSize, {deltaList.pop;})});
		^deltaList;
	}

	reset { started=false }

}
