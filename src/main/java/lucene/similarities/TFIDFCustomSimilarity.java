package lucene.similarities;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;

public class TFIDFCustomSimilarity extends TFIDFSimilarity {

	public TFIDFCustomSimilarity() {
		
	}

	@Override
	public float coord(int overlap, int maxOverlap) {
		return 1;
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		return 1;
	}

	@Override
	public float tf(float freq) {
		return freq;
	}

	@Override
	public float idf(long docFreq, long numDocs) {
		return (float) Math.sqrt((Math.log(numDocs / (double) docFreq)));
	}

	@Override
	public float lengthNorm(FieldInvertState state) {
		return 1;
	}

	@Override
	public float sloppyFreq(int distance) {
		return 1.0f / (distance + 1);
	}

	@Override
	public float scorePayload(int doc, int start, int end, BytesRef payload) {
		return 1;
	}

}
