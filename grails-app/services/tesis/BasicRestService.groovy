package tesis

import com.ning.http.client.AsyncHttpClient
import com.ning.http.client.FluentStringsMap
import com.ning.http.client.Response
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder
import com.ning.http.client.AsyncHttpClientConfig


class BasicRestService {

	static transactional = false

	private AsyncHttpClient http = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeoutInMs(100000).build());
	

	private BoundRequestBuilder prepareGet(String path, FluentStringsMap params) {
		//log.info("Adding headers & query params.")
		return http.prepareGet(path)
		.addHeader("Accept", "application/json")
		.setQueryParameters(params);
	}

	public Response get(String path, FluentStringsMap params){
		
		BoundRequestBuilder r = prepareGet(path, params);

		Response response;
		try {
			response = r.execute().get();
		} catch (Exception e) {
			println path + params
			println e.message
		}

		return response;
	}
}