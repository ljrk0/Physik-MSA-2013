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
	Field friction_velue# ; scalar
End Type

;------------------------------------------------------------
; PH_DOTICK
; Calulates basic Tick-Relevated things like acceleration,
;	velocity and Position
;-------------------------------------------------------------


Function Ph_DoTick(Obj.Ph_Object, Time#)
	
	Local a#[1]
	; Apply Acceleration to Velocity
	
	MultiplyVector(Obj\Acc, Time, a)
	
	AddVector(Obj\Vel,a,Obj\Vel)
	Obj\RotVel=Obj\RotVel+Obj\RotAcc*Time
	
	; Apply Velocity to Position
	
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
;Applys an Force at a point of an object - use this to
; apply Forces
;----------------------------------------------------------

Function Ph_ApplyForce(Obj.Ph_Object, Force#[1], approach#[1], Relative = True)
	If Obj\Fixed Then Return
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
;Applyes an Force, that directly cause an Acceleration
;-------------------------------------------------------

Function Ph_ApplyVelForce(Obj.Ph_Object, Force#[1])
	If Obj\Fixed Then Return
	Local a#[1]
	DivideVector(Force,Obj\Mass, a)
	AddVector(Obj\Acc,a,Obj\Acc)
End Function

;-------------------------------------------------------
; PH_APPLYROTTORQUE
; Applys an Torque to an Object
;-------------------------------------------------------

Function Ph_ApplyRotTorque(Obj.Ph_Object, Torque#)
	If Obj\Fixed Then Return
	Obj\RotAcc=Obj\RotAcc+Torque/Obj\RotMass
End Function

;-------------------------------------------------------
; PH_RENDER
; Draws the Object on the Screen
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
; Copyses an Object needed for Collision-Check
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
	
	If Not obj\Fixed Then Ph_DoTick(result, t)
	Return result
End Function

;-----------------------------------------------------------
; PH_READFROMFILE
; Reads Szene form a File
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
				RuntimeError "Object Form " + Form + " unknown"
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
	Wend
	
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D