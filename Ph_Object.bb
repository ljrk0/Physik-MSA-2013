Include "Ph_CollisionBox.bb"


Type Ph_Object
	Field Image
	Field Pos#[1]   ; m
	Field Vel#[1]   ; m/s
	Field Acc#[1]   ; m/s²
	Field Rot#      ; rad
	Field RotVel#   ; rad/s
	Field RotAcc#   ; rad/s²
	Field Mass#     ; kg
	Field RotMass#  ; kg/px²
	Field CollisionBox.Shape
	Field Virtual   ; true/false
	Field Fixed		; true/false
	Field friction_velue# ; scalar                                                        !!!value
	Field FixedPos#[1], FixedRot#
	Field maxTick# ;true/false
End Type

;------------------------------------------------------------
; PH_DOTICK
; Calculates basic tick-related things eg. acceleration,         
;-------------------------------------------------------------


Function Ph_DoTick(Obj.Ph_Object, Time#)
	If Obj\maxTick < Time And Obj\maxTick > 0 Then Time = Obj\maxTick
	Obj\maxTick=0
	
	If Obj\Fixed Then
		Obj\Vel[0] = 0
		Obj\Vel[1] = 0
		Obj\RotVel = 0
		; reset Acceleration
		Obj\Acc[0] = 0
		Obj\Acc[1] = 0
		Obj\RotAcc = 0
		Return
	EndIf
	
	Local a#[1]
	; Apply acceleration to velocity 
	
	MultiplyVector(Obj\Acc, Time, a)
	
	AddVector(Obj\Vel,a,Obj\Vel)
	Obj\RotVel=Obj\RotVel+Obj\RotAcc*Time
	
	; Apply velocity to position
	
	MultiplyVector(Obj\Vel, Time, a)
	
	AddVector(Obj\Pos,a,Obj\Pos)
	Obj\Rot=Obj\Rot+Obj\RotVel*Time
	; reset Acceleration
	Obj\Acc[0] = 0
	Obj\Acc[1] = 0
	Obj\RotAcc = 0
End Function

;----------------------------------------------------------
;PH_APPLYFORCE
;Applys a force at a point of an object - use this to
; apply forces
;----------------------------------------------------------

Function Ph_ApplyForce(Obj.Ph_Object, Force#[1], approach#[1], Relative = True)
	If Relative Then
		RotateVector(Force,Obj\Rot,Force)
		RotateVector(approach,Obj\Rot,approach)
	EndIf
	
	If VectorLenght(Force)=0 Then Return
	If VectorLenght(approach)=0 Then
		Ph_ApplyVelForce(Obj,Force)
	Else
		
		
			
		Local a#[1]
		MultiplyVector(approach,-1,a)
		Local Angle# = RadToDeg(VectorAngle(a,Force))
		Ph_ApplyRotTorque(Obj,Sin(Angle)*VectorLenght(Force)*VectorLenght(approach))
		Local b#[1]
		NormalizeVector(a,b)
		
		MultiplyVector(b, Sin(90-Angle)*VectorLenght(Force),b)
		
		Ph_ApplyVelForce(Obj,b)
	EndIf
	
End Function

;-------------------------------------------------------
;PH_APPLYVELFORCE
;Applys a force, that directly causes an acceleration
;-------------------------------------------------------

Function Ph_ApplyVelForce(Obj.Ph_Object, Force#[1])
	Local a#[1]
	DivideVector(Force,Obj\Mass, a)
	AddVector(Obj\Acc,a,Obj\Acc)
End Function

;-------------------------------------------------------
; PH_APPLYROTTORQUE
; Applys a torque to an object
;-------------------------------------------------------

Function Ph_ApplyRotTorque(Obj.Ph_Object, Torque#)
	Obj\RotAcc=Obj\RotAcc+Torque/Obj\RotMass
End Function

;-------------------------------------------------------
; PH_RENDER
; Draws the object on-screen
;-------------------------------------------------------

Function Ph_Render(Obj.Ph_Object)   ; 1 m ^= 100px
	Ph_DrawImagefromCollisonBox(Ph_GetAbsolutCollisionBox(Obj))
	Local temp#[1]
	temp[0]=20
	temp[1]=0
	Color 100,100,255
	Plot Obj\Pos[0]*100,Obj\Pos[1]*100
	Color 255,255,0
	RotateVector(temp, Obj\Rot, temp)
	Plot Obj\Pos[0]*100 + temp[0],Obj\Pos[1]*100 + temp[1]
End Function

;----------------------------------------------------------
; PH_GETVIRTUALCOPYAFTERTIME
; Copys an object needed for collision-checking
;----------------------------------------------------------

Function Ph_GetVirtualCopyAfterTime.Ph_Object(obj.Ph_Object,t#)
	Local result.Ph_Object = New Ph_Object
	result\Acc[0] = obj\Acc[0]
	result\Acc[1] = obj\Acc[1]
	result\CollisionBox = obj\CollisionBox
	result\Mass = obj\Mass
	result\Pos[0] = obj\Pos[0]
	result\Pos[1] = obj\Pos[1]
	result\Rot = obj\Rot
	result\RotAcc = obj\RotAcc
	result\RotMass = obj\RotMass
	result\RotVel = obj\RotVel
	result\Vel[0] = obj\Vel[0]
	result\Vel[1] = obj\Vel[1]
	result\Virtual = True
	
	Ph_DoTick(result, t)
	Return result
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
		Obj\friction_velue = ReadLine(File)
		Obj\Fixed = ReadLine(File)
		If Obj\Fixed Then
			Obj\FixedPos[0]=Obj\Pos[0]
			Obj\FixedPos[1]=Obj\Pos[1]
			Obj\FixedRot=Obj\Rot
		EndIf
	Wend
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D