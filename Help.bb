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
	V2[0]=V1[0]/VectorLenght(V1)
	V2[1]=V1[1]/VectorLenght(V1)
End Function

Function VectorScalarProduct#(V1#[1], V2#[1])
	Return (V1[0]*V2[0])+(V1[1]*V2[1])
End Function

Function VectorCrossProduct#(V1#[1], V2#[1])
	Return (V1[0]*V2[1])-(V1[1]*V2[0])
End Function

Function VectorLenght#(V1#[1])
	Return Sqr((V1[0]^2)+(V1[1]^2)) 
End Function


Function VectorAngle#(V1#[1], V2#[1])
	If VectorCrossProduct(V1,V2) > 0 Then
		Return DegToRad(-ACos(VectorScalarProduct(V1,V2)/(VectorLenght(V1)*VectorLenght(V2))))
	Else
		Return DegToRad(ACos(VectorScalarProduct(V1,V2)/(VectorLenght(V1)*VectorLenght(V2))))
	EndIf	
End Function

Function RotateVector(V1#[1],theta#,V2#[1])
	theta = RadToDeg(theta)
	
	Local X#, Y#
	X = V1[0] * Cos(theta) - V1[1] * Sin(theta)
	Y = V1[0] * Sin(theta) + V1[1] * Cos(theta)
	
	;Print "------"
	;Print theta
	;Print V1[0]
	;Print V1[1]
	;Print X
	;Print Y
	;WaitKey
	V2[0] = X
	V2[1] = Y
End Function

Function RadToDeg#(rad#)
	Return rad * (180/Pi)
End Function

Function DegToRad#(deg#)
	Return deg*(Pi/180)
End Function

;Function IsIntersecting(a#[1], b#[1], c#[1], d#[1])
;	Local denominator# = ((b[0] - a[0]) * (d[1] - c[1])) - ((b[1] - a[1]) * (d[0] - c[0]))
;	Local numerator1# = ((a[1] - c[1]) * (d[0] - c[0])) - ((a[0] - c[0]) * (d[1] - c[1]))
;	Local numerator2# = ((a[1] - c[1]) * (b[0] - a[0])) - ((a[0] - c[0]) * (b[1] - a[1]))
;	
;	If (denominator = 0) Then Return numerator1 = 0 And numerator2 = 0
;	
;	Local r# = numerator1 / denominator
;	Local s# = numerator2 / denominator
;	
;    Return (r >= 0 And r <= 1) And (s >= 0 And s <= 1)
;End Function

Function IsIntersecting(P1#[1], P2#[1], P3#[1], P4#[1], CP#[1])
	
	
	
	
	Local m1#;
	Local t1;
	Local m2#;
	Local t2;
	
	;Local xs#, ys#
	If P1[0]=P2[0] Then   ; 1. Strecke keine Funktion (m=unendlich)
		If P3[0]=P4[0] Then   ; 2. Strecke keine Funktion
			If P1[0]=P3[0] Then
				; Strecken liegen in einer Geraden
				Return False
			Else
				; Strecken haben keinen schnittpunkt (sind parallel)
				Return False;
			EndIf
		Else 
			m2=(P3[1]-P4[1])/(P3[0]-P4[0])
			t2=P3[0]-(m2*P3[0])
			CP[0]=P1[0]
			CP[1]=(CP[0])*m2+t2
		EndIf
	Else
		If P3[0]=P4[0] Then
			m1=(P1[1]-P2[1])/(P1[0]-P2[0])
			t1=P1[1]-(m1*P1[0])
			CP[0]=P3[0];
			CP[1]=(CP[0]*m1)+t1
		Else
			m1=(P1[1]-P2[1])/(P1[0]-P2[0])
			t1=P1[1]-(m1*(P1[0]))
			m2=(P3[1]-P4[1])/(P3[0]-P4[0])
			t2=P3[1]-(m2*P3[0])
			;endif?
			If m1=m2 Then   ; gleiche Steigung, heikel, da m1, m2 floats (siehe hilfedatei)
						; evtl. workaround:
				If m1*1000=m2*1000 Then
					If t1=t2 Then   ; gleicher y-Abschnitt
						; Strecken liegen in einer Geraden
						Return 0
					Else 
						; Strecken sind parallel
						Return 0;
					EndIf
				EndIf
				CP[0]=(t2-t1)/(m1-m2)
				CP[1]=(CP[0]*m2)+t2
			EndIf
		EndIf
		Return True;
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D