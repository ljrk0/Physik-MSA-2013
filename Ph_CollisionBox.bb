Include "Shape.bb"

;---------------------------------------------------
;PH_CALCULATEMOMENTOFINERTIA
; Calculates the Moment Of Ineria from a Mass and
; a Collision-Box/Shape
;---------------------------------------------------
Function Ph_CalculateMomentOfInertia#(Obj.Shape, mass#)
	Local Sum1#, Sum2#
	Local i
	For i=0 To Obj\PointAnz-1
		Sum1=Sum1+Abs(VectorCrossProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[i]\Pos))*(VectorScalarProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[(i+1) Mod Obj\PointAnz]\Pos) + VectorScalarProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[i]\Pos) + VectorScalarProduct(Obj\Point[i]\Pos,Obj\Point[i]\Pos))
		Sum2=Sum2+Abs(VectorCrossProduct(Obj\Point[(i+1) Mod Obj\PointAnz]\Pos,Obj\Point[i]\Pos))
	Next
	Return ((mass/6)*(Sum1/Sum2))
End Function

;-----------------------------------------------------
;PH_GETABSOLUTCOLLISIONBOX
; Transfers an CollisonBox to the gloal
; Coordiat-System
;-----------------------------------------------------
Function Ph_GetAbsolutCollisionBox.Shape(obj.Ph_Object)
	Local obj2.Shape = obj\CollisionBox ; nur zur vereinfachten Schreibweise
	Local result.Shape = New Shape
	result\PointAnz = obj2\PointAnz
	Local i
	For i = 0 To obj2\PointAnz-1
		result\Point[i] = New Point
		RotateVector(obj2\Point[i]\Pos, obj\Rot, result\Point[i]\Pos)
		AddVector(result\Point[i]\Pos,obj\Pos,result\Point[i]\Pos)
	Next
	Return result
End Function

;-------------------------------------------------------
;PH_RELATIVEPOSITION
; Transfers an Global Coordinate to the objects
; Coordination-System
;-------------------------------------------------------

Function Ph_RelativatePosition(obj.Ph_Object, P1#[1], P2#[1])
	SubtractVector(P1,obj\Pos,P2)
	RotateVector(P2,-obj\Rot,P2)
End Function

;------------------------------------------------------
;PH_COLLISIONBOXCOLLIDING
; Checks, if tow (absolute) Collisionboxes are
; collidiong Or Not
;------------------------------------------------------

Function Ph_CollisionBoxColliding(obj1.Shape,obj2.Shape)
	Local i,j
	Local count = 0
	Local temp#[1]
	Local temp2#[1]
	Local Pos0#[1]
	Local Pos1#[1]
	Cls
	For i=1 To obj1\PointAnz
		For j=1 To obj2\PointAnz
			If lineLine(obj1\Point[i Mod obj1\PointAnz]\Pos, obj1\Point[i-1]\Pos, obj2\Point[j Mod obj2\PointAnz]\Pos, obj2\Point[j-1]\Pos, temp) Then
				If count=0 Then
					Pos0[0] = temp[0]
					Pos0[1] = temp[1]
				Else
					Pos1[0] = temp[0]
					Pos1[1] = temp[1]
				EndIf
				
				count=count+1
			EndIf
			;If count=>2 Then Exit
		Next
		
;		Print count
;		Print obj1\Point[i Mod obj1\PointAnz]\Pos[0]*50+100 + ", " + obj1\Point[i Mod obj1\PointAnz]\Pos[1]*50+100 + ", " + obj1\Point[i-1]\Pos[0] + ", " + obj1\Point[i-1]\Pos[1]*50+100 
;		WaitKey()
		
		;If count=>2 Then Exit
	Next
	
	If count=>2 Then
		Local Bank = CreateBank(12)
		
		
		
		
		AddVector(Pos0,Pos1,temp)
		DivideVector(temp,2,temp)
		PokeFloat Bank, 0, temp[0]
		PokeFloat Bank, 4, temp[1]
		
		temp2[0]=1
		temp2[1]=0
		
		
		
		SubtractVector(Pos1,Pos0,temp)
		PokeFloat Bank, 8, VectorAngle(temp,temp2)
		
		
		
		
		
		
		Return Bank
	Else 
		Return 0
	EndIf
End Function

;----------------------------------------------
;PH_DRAWIMAGEFROMCOLLISIONBOX
; Draws an Collision-Box on the screen
;----------------------------------------------
Function Ph_DrawImagefromCollisonBox(Obj.Shape)
	Local i
	Local r=ColorRed()
	Local g=ColorGreen()
	Local b=ColorBlue()
	Color 255,0,0
	Line Obj\Point[Obj\PointAnz-1]\Pos[0]*100,Obj\Point[Obj\PointAnz-1]\Pos[1]*100,Obj\Point[i]\Pos[0]*100,Obj\Point[i]\Pos[1]*100
	For i = 1 To Obj\PointAnz-1
		Line Obj\Point[i-1]\Pos[0]*100,Obj\Point[i-1]\Pos[1]*100,Obj\Point[i]\Pos[0]*100,Obj\Point[i]\Pos[1]*100
		DebugLog Obj\Point[i-1]\Pos[0]*100
	Next
	SetBuffer BackBuffer()
	Color r,g,b
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D