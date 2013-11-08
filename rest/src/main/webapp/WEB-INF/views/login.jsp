<!DOCTYPE HTML>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>TripMeter Login</title>
	</head>
	<body>
	    <c:if test="${error ne null}">
            <div style="color:#D8000C;background-color:#FFBABA;">${error}</div>
        </c:if>
		<h1>Login!</h1>
		<c:if test="${init ne null}">
		    ${init} works!
            <form id="loginForm" action="/trip/authorise" method ="POST">
                Enter your login id: <input type="text" name="email"/>
                <input type="submit" value="Get Auth Code"/>
            </form>
        </c:if>
		<c:if test="${authCode ne null}">
		<form id="codeForm" action="/trip/get_token" method ="POST">
            Auth Code: <input type="text" name="authCode" value="${authCode}" />
            State: <input type="text" name="state" value="${state}" />
            <input type="submit" value="Get Auth Token"/>
        </form>
        </c:if>
        <c:if test="${accessToken ne null}">
            Get Details:
             <form id="tokenForm" action="/trip/get_details" method ="POST">
                 Access Token: <input type="text" name="accessToken" value="${accessToken}" />
                 Expires In: <input type="text" name="expiresIn" value="${expiresIn}" />
                 <input type="submit" value="Get Details"/>
             </form>
        </c:if>
        <c:if test="${details ne null}">
                    Details: ${details}
                </c:if>

	</body>
</html>
