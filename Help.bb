;---------------------------------------------
;ADDVECTOR;SUBTRACTVECTOR
;DIVIDEVECTOR
;MULTIPLYVECTOR
;NORMALIZEVECTOR
;VECTORSCALARPRODUCT
;VECTORCROSSPRODUCT
;VECTORLength
;VECTORANGLE
;ROTATEVECTOR
; Basic Vector Functions
;----------------------------------------------



Function AddVector(V1#[1],V2#[1],V3#[1])
	V3[0]=V1[0]+V2[0]
	V3[1]=V1[1]+V2[1]
End Function

Function SubtractVector(V1#[1],V2#[1],V3#[1])
	V3[0]=V1[0]-V2[0]
	V3[1]=V1[1]-V2[1]
End Function

Function DivideVector(V1#[1],Z#,V2#[1])
	V2[0]=V1[0]/Z
	V2[1]=V1[1]/Z
End Function

Function MultiplyVector(V1#[1],Z#,V2#[1])
	V2[0]=V1[0]*Z
	V2[1]=V1[1]*Z
End Function

Function NormalizeVector(V1#[1],V2#[1])
	V2[0]=V1[0]/VectorLength(V1)                                       
	V2[1]=V1[1]/VectorLength(V1)
End Function

Function VectorScalarProduct#(V1#[1], V2#[1])
	Return (V1[0]*V2[0])+(V1[1]*V2[1])
End Function

Function VectorCrossProduct#(V1#[1], V2#[1])
	Return (V1[0]*V2[1])-(V1[1]*V2[0])
End Function

Function VectorLength#(V1#[1])
	Return Sqr((V1[0]^2)+(V1[1]^2)) 
End Function


Function VectorAngle#(V1#[1], V2#[1])
	If VectorCrossProduct(V1,V2) > 0 Then
		Return DegToRad(-ACos(VectorScalarProduct(V1,V2)/(VectorLength(V1)*VectorLength(V2))))
	Else
		Return DegToRad(ACos(VectorScalarProduct(V1,V2)/(VectorLength(V1)*VectorLength(V2))))
	EndIf	
End Function

Function RotateVector(V1#[1],theta#,V2#[1])
	theta = RadToDeg(theta)
	Local X#, Y#
	X = V1[0] * Cos(theta) - V1[1] * Sin(theta)
	Y = V1[0] * Sin(theta) + V1[1] * Cos(theta)
	
	V2[0] = X
	V2[1] = Y
End Function

;--------------------------------------------
;RADTODEG
;DEGTORAD
; Converts degree to radiant and inverse
;---------------------------------------------

Function RadToDeg#(rad#)
	Return rad * (180/Pi)
End Function

Function DegToRad#(deg#)
	Return deg*(Pi/180)
End Function

;-------------------------------------------------
; LINELINE
; Checks wether the two lines are colliding
;------------------------------------------------

Function LineLine(p0#[1], p1#[1], p2#[1], p3#[1], result#[1])
	
	
	Local n# = (p0[1]-p2[1])*(p3[0]-p2[0]) - (p0[0]-p2[0])*(p3[1]-p2[1])
	Local d# = (p1[0]-p0[0])*(p3[1]-p2[1]) - (p1[1]-p0[1])*(p3[0]-p2[0])
	Local Sn#, AB#, CD#
	If Abs(d#) < 0.0001 
		; Lines are parallel!
		Return False
	Else
		; Lines might cross!
		Sn# = (p0[1]-p2[1])*(p1[0]-p0[0]) - (p0[0]-p2[0])*(p1[1]-p0[1])
		
		AB# = n# / d#
		If AB#=>0.0 And AB#<=1.0
			CD# = Sn# / d#
			If CD#=>0.0 And CD#<=1.0
				; Intersection Point
				result[0] = p0[0] + AB#*(p1[0]-p0[0])
		       	result[1] = p0[1] + AB#*(p1[1]-p0[1])
				Return True
			End If
		End If
		
		; Lines don't cross, because the intersection is beyond the endpoints of the lines   
	EndIf
	
	; Lines do not cross!
	Return False
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D