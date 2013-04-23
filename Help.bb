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
	
	V2[0] = X
	V2[1] = Y
End Function

Function RadToDeg#(rad#)
	Return rad * (180/Pi)
End Function

Function DegToRad#(deg#)
	Return deg*(Pi/180)
End Function

Function lineLine(P1#[1],P2#[1],P3#[1],P4#[1], CP#[1] )
	
	
	Local Ax#,Bx#,Cx#,Ay#,By#,Cy#,d#,e#,f#,num#,offset#;

	Local x1lo#,x1hi#,y1lo#,y1hi#;

	
	Ax = P2[0]-P1[0];

	Bx = P3[0]-P4[0];



	; X bound box test/

	If(Ax<0) Then
	
		x1lo=P2[0];
		x1hi=P1[0];
	
    Else
	
		x1hi=P2[0];
		x1lo=P1[0];
		
	EndIf
	
    If(Bx>0) Then
		
        If(x1hi < P4[0] Or P3[0] < x1lo) Then Return False;
			
	Else
		If(x1hi < P3[0] Or P4[0] < x1lo) Then Return False;
	EndIf
	Ay = P2[1]-P1[1];

	By = P3[1]-P4[1];
	
		
		
	;Y bound box test//
		
	If(Ay<0) Then                  
		
		y1lo=P2[1];
		y1hi=P1[1];
		
	Else
		
		y1hi=P2[1];
		y1lo=P1[1];
		
	EndIf
		
	If(By>0) Then
		If(y1hi < P4[1] Or P3[1] < y1lo) Then Return False;
	Else 
		If(y1hi < P3[1] Or P4[1] < y1lo) Then Return False;
	EndIf			
	
	Cx = P1[0]-P3[0];
	Cy = P1[1]-P3[1];
	
	d = By*Cx - Bx*Cy;  // alpha numerator//
	
	f = Ay*Bx - Ax*By;  // both denominator//
	
	
	
	; alpha tests//
			
	If(f>0) Then
		
		If(d<0 Or d>f) Then Return False;
	Else
		If(d>0 Or d<f) Then Return False;
	EndIf
	
	e = Ax*Cy - Ay*Cx;  // beta numerator//

	; beta tests //
		
	If(f>0) Then                           
		
		If(e<0 Or e>f) Then Return False;
			
	Else
			
		If(e>0 Or e<f) Then Return False
	EndIf		
			; check If they are parallel
			
	If(f=0) Then Return False;
									
										
										
	; compute intersection coordinates //
	
	num = d*Ax; // numerator //
	
	If same_sign(num,f) Then offset = f*0.5 Else offset = -f*0.5
	
	CP[0] = P1[0] + (num+offset) / f;
	
	
	
	num = d*Ay;
	
	If same_sign(num,f) Then offset = f*0.5 Else offset = -f*0.5
	
	CP[1] = P1[1] + (num+offset) / f;
	
	
	Return True;
	
End Function
																
																
																
Function same_sign( a#, b# )

    Return ( ( a * b ) >= 0 );

End Function
;~IDEal Editor Parameters:
;~C#Blitz3D