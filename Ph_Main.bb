Include "Help.bb"
Include "Ph_Object.bb"
Include "Ph_Collision.bb"

;-----------------------------------------------------
; MAINPHYSICTICK
; Calls the DoTick-Funciton for each Object
;-----------------------------------------------------

Function MainPhysicTick(t#)
	Local obj.Ph_Object
	Local obj2.Ph_Force
	Ph_DoCollision(t,0.000000001)
	For obj = Each Ph_Object
		Ph_DoTick(obj, t)
	Next
	
	For obj2 = Each Ph_Force
		If obj2\time = 0 Then
			Delete obj2
		Else
			Ph_ApplyForce(obj2\Obj, obj2\Force, obj2\approach, obj2\Relative)
			obj2\time=obj2\time-1
		EndIf	
	Next
	
End Function

;------------------------------------------------------
; MAINPHYSICSRENDER
; Calls the Render-Function for each Object
;------------------------------------------------------

Function MainPhysicRender()
	Local obj.Ph_Object
	Local obj2.Ph_Force
	For obj = Each Ph_Object
		If obj\CollisionBox <> Null Then Ph_Render(obj)
	Next
	Color 255,255,0
	Local Force2#[1]
	Local approach2#[1]
	For obj2 = Each Ph_Force
		If obj2\Relative Then
			RotateVector(obj2\Force,obj2\Obj\Rot,Force2)
			RotateVector(obj2\approach,obj2\Obj\Rot,approach2)
		Else
			Force2[0]=obj2\Force[0]
			Force2[1]=obj2\Force[1]
			approach2[0]=obj2\approach[0]
		EndIf
		Line approach2[0]*100+obj2\Obj\Pos[0]*100,approach2[1]*100+obj2\Obj\Pos[1]*100,approach2[0]*100 + Force2[0]*20 + obj2\Obj\Pos[0]*100,approach2[1]*100 + Force2[1]*20 + obj2\Obj\Pos[1]*100
	Next
End Function



;-----------------------------------------------------------
; PH_READFROMFILE
; Reads scene from a file
; -----------------------
;  File Structure:
;  /----------------\
;  |[Beliebig]		|
;  |square/cycle	|
;  |if square: XSize|
;  |		   YSize|
;  |if cycle: radius|
;  |XPos			|
;  |YPos			|
;  |XVel			|
;  |YVel			|
;  |Rot				|
;  |RotVel			|
;  |Mass			|
;  |fruction_value	|
;  |fixed [0|1]     |
;  |[Beliebig]		|
;  |[... Obj2 ...]  |
;  |[...]			|
;  \----------------/
;-----------------------------------------------------------


Function Ph_ReadFromFile(Filename$)	
	Local File = ReadFile (Filename)
	Local Form$
	While Not Eof(File)
		Local Obj.Ph_Object = New Ph_Object
		ReadLine(File)
		Form$ =  ReadLine(File)
		Select Form
			Case "square"
				Local XSize# = ReadLine(File)
				Local YSize# = ReadLine(File)
				Obj\CollisionBox = Sh_CreateSquare(-XSize, -YSize, XSize, YSize)
			Case "cycle"
				Obj\CollisionBox = Sh_CreateCycle(0,0,ReadLine(File))
			Default
				Delete Obj
				Return
		End Select
		Obj\Pos[0] = ReadLine(File)
		Obj\Pos[1] = ReadLine(File)
		Obj\Vel[0] = ReadLine(File)
		Obj\Vel[1] = ReadLine(File)
		Obj\Rot = ReadLine(File)
		Obj\RotVel = ReadLine(File)
		Obj\Mass = ReadLine(File)
		Obj\RotMass = Ph_CalculateMomentOfInertia(Obj\CollisionBox,Obj\Mass)
		Obj\friction_value = ReadLine(File)
		Obj\Fixed = ReadLine(File)
		If Obj\Fixed Then
			Obj\FixedPos[0]=Obj\Pos[0]
			Obj\FixedPos[1]=Obj\Pos[1]
			Obj\FixedRot=Obj\Rot
		EndIf
		Local L1$
		Repeat
			L1$ = ReadLine(File)
			If L1$ = "End" Then Exit
			Local Obj2.Ph_Force = New Ph_Force
			Obj2\Force[0] = L1$
			Obj2\Force[1] = ReadLine(File)
			Obj2\approach[0] = ReadLine(File)
			Obj2\approach[1] = ReadLine(File)
			Obj2\Relative = ReadLine(File)
			Obj2\Obj = Obj
			Obj2\time = 1000
		Forever
	Wend
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D