package tesis

import org.springframework.web.context.request.RequestContextHolder

class SessionService {
	
	def getSession(){
		return RequestContextHolder.currentRequestAttributes().getSession() 
	}
	
	def init (){
		getSession().index = null
	}
	
	def getId(){
		getSession().id
	}	
	
	def setIndex(index){
		getSession().index = index
	}
	
	def getIndex(){
		getSession().index
	}

}

