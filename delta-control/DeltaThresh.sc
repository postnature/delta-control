//(c) 2020 Erik Nyström, published under GNU General Public License, GPLv3.

DeltaThresh {

	// the funcs are passed the arguments vel (velocity, i.e. delta), and val (an arbitrary value, e.g. position)
	var <>minDelta=0.01, <>maxDelta=1, <>threshold=0.2, slowFunc, fastFunc;
	var deltas;

	*new { | minDelta=0.01, maxDelta=1, threshold=0.2, slowFunc, fastFunc |

		^super.newCopyArgs(minDelta, maxDelta, threshold, slowFunc, fastFunc).init;

	}

	init { deltas=DeltaList(1,minDelta, maxDelta); deltas.addDelta }

	funkExc {|val|

		var vel;
		deltas.addDelta;
		vel=deltas.deltaList[0];
		"func select vel: ".postf;vel.postln;
		if(vel<threshold, {fastFunc.(vel,val)}, {slowFunc.(vel,val);});
	}

	//if below threshold add the fast one. Always play the slow one

    funkAddFast {|val|

		var vel;
		deltas.addDelta;
		vel=deltas.deltaList[0];
		if(vel<threshold, {fastFunc.(vel,val);slowFunc.(vel,val)}, {slowFunc.(vel,val);});
	}

	//if above threshold add the slow one. Always play the fast one

	funkAddSlow {|val|

		var vel;
		deltas.addDelta;
		vel=deltas.deltaList[0];
		if(vel<threshold, {fastFunc.(vel,val);}, {fastFunc.(vel,val);slowFunc.(vel,val);});
	}

}
