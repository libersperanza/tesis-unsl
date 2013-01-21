package tesis

import org.springframework.web.context.request.RequestContextHolder

class SessionService {
	
	def getSession(){
		return RequestContextHolder.currentRequestAttributes().getSession() 
	}
	
	def init (){
		getSession().categs = null
		getSession().pivots = null	
	}
	
	def getId(){
		getSession().id
	}	
	
	def setCategs(categs){
		getSession().categs = categs
	}
	
	def setPivots(pivots){
		getSession().pivots = pivots
	}
	
	def getCategs(){
		getSession().categs
	}
	
	def getPivots(){
		getSession().pivots
	}

}

