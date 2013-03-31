package lucene;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.util.BytesRef;


public class CustomSimilarity extends TFIDFSimilarity {

	@Override
	public float coord(int overlap, int maxOverlap) {
		return overlap / (float) maxOverlap;
	}

	@Override
	public float queryNorm(float sumOfSquaredWeights) {
		return (float) (1.0 / Math.sqrt(sumOfSquaredWeights));
	}

	@Override
	public float tf(float freq) {
//		return (float) Math.sqrt(freq);
		return 1.0f;
	}

	@Override
	public float idf(long docFreq, long numDocs) {
		return (float) (Math.log(numDocs/(double)(docFreq+1)) + 1.0);
	}

	@Override
	public float lengthNorm(FieldInvertState state) {
		final int numTerms = state.getLength();
		return state.getBoost() * ((float) (1.0 / Math.sqrt(numTerms)));
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
