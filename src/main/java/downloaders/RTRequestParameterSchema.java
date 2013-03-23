package downloaders;

public interface RTRequestParameterSchema extends ParameterSchema {
	public final String PARAM_LIMIT = "limit";
	public final String PARAM_PAGE_LIMIT = "page_limit";
	public final String PARAM_PAGE = "page";
	public final Integer PARAM_LIMIT_DEFAULT = 50;
	public final Integer PARAM_PAGE_LIMIT_DEFAULT = 50;
	public final String PARAM_API_KEY = "apikey";

}
