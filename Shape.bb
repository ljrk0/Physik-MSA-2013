Type Shape
	Field PointCount
	Field Point.Point[100]
End Type

Type Point
	Field Pos#[1]
End Type

;-------------------------------------------
;SH_ADDPOINT
; Adds a point to an existing shape
;-------------------------------------------
Function Sh_AddPoint(Obj.Shape,X#,Y#)
	Obj\Point[Obj\PointCount]=New Point
	Obj\Point[Obj\PointCount]\Pos[0]=X
	Obj\Point[Obj\PointCount]\Pos[1]=Y
	Obj\PointCount=Obj\PointCount+1
	
End Function

;-------------------------------------------
;SH_CREATESQUARE
;Crates a new shape as a square                  
;-------------------------------------------
Function Sh_CreateSquare.Shape(X1#,Y1#,X2#,Y2#)
	Local CBox.Shape = New Shape
	Sh_AddPoint(CBox,X1,Y1)
	Sh_AddPoint(CBox,X2,Y1)
	Sh_AddPoint(CBox,X2,Y2)
	Sh_AddPoint(CBox,X1,Y2)
	Return CBox
End Function

;-------------------------------------------
;SH_CREATECYCLE
;Creates a new Shape as a cycle                    
;-------------------------------------------
Function Sh_CreateCycle.Shape(X#,Y#,r#)
	Local CCyc.Shape = New Shape
	Local i
	For i=0 To 350 Step 10
		Sh_AddPoint(CCyc,Sin(i)*r + X,Cos(i)*r + Y)
	Next
	Return CCyc
End Function

;---------------------------------------------
;SH_GETSHAPEEGE
;Gets the minimum and maximum Points of the            
;shape
;---------------------------------------------
Function Sh_GetShapeEge(Obj.Shape, minPoint#[1], maxPoint#[1])
	Local i
	
	minPoint[0]=10000
	minPoint[1]=10000
	maxPoint[0]=-10000
	maxPoint[1]=-10000
	
	For i = 0 To Obj\PointCount-1
		If Obj\Point[i]\Pos[0] < minPoint[0] Then minPoint[0] = Obj\Point[i]\Pos[0]
		If Obj\Point[i]\Pos[0] > maxPoint[0] Then maxPoint[0] = Obj\Point[i]\Pos[0]
		If Obj\Point[i]\Pos[1] < minPoint[1] Then minPoint[1] = Obj\Point[i]\Pos[1]
		If Obj\Point[i]\Pos[1] > maxPoint[1] Then maxPoint[1] = Obj\Point[i]\Pos[1]
	Next
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D