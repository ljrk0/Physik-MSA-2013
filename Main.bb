Graphics 800,600,0,6
SetBuffer BackBuffer()
AppTitle "MSA 2013 - Physicsimulationen in der Informatik - Copuright (c) 2013 by Jochen Jacobs & Leonard Robert Koenig"

Include "Help.bb"
Include "Ph_Main.bb"

Local Testobject1.Ph_Object = New Ph_Object
Testobject1\Pos[0] = 1
Testobject1\Pos[1] = 1.2
Testobject1\Mass = 5

Local CBox.Shape = Sh_CreateSquare(-0.4,-1,0.4,1)
Testobject1\CollisionBox = CBox
Testobject1\Image = Ph_CreateImagefromCollisonBox(CBox)
Testobject1\RotMass = Ph_CalculateMomentOfInertia(CBox,Testobject1\Mass)


Local Testobject2.Ph_Object = New Ph_Object
Testobject2\Pos[0] = 1
Testobject2\Pos[1] = 4.2
Testobject2\Rot = Pi * 0.25
Testobject2\Mass = 5

Local CBox2.Shape = Sh_CreateSquare(-0.4,-1,0.4,1)
Testobject2\CollisionBox = CBox2
Testobject2\Image = Ph_CreateImagefromCollisonBox(CBox2)
Testobject2\RotMass = Ph_CalculateMomentOfInertia(CBox2,Testobject1\Mass)


Local force1#[1]
Local pos1#[1]
Local force2#[1]
Local pos2#[1]

Local LastTime# = MilliSecs()

Repeat
	
	Cls
	
	
	force1[0] = 0
	force1[1] = 0.1
	
	pos1[0] = 0.3
	pos1[1] = 1
	
	
	Ph_ApplyForce(Testobject1, force1, pos1)
	
	
	force2[0] = 0
	force2[1] = 0.1
	pos2[0] = -0.3
	pos2[1] = 1
	
	Ph_ApplyForce(Testobject1, force2, pos2)
	
	
	
	Ph_DoCollision((MilliSecs()-LastTime)/1000,0.01)
	
	Ph_DoTick(Testobject1, (MilliSecs()-LastTime)/1000)
	
	LastTime=MilliSecs()
	
	
	
	Ph_Render(Testobject1)
	Ph_Render(Testobject2)
	
	
	Color 255,255,0
	Line pos1[0]*100+Testobject1\Pos[0]*100,pos1[1]*100+Testobject1\Pos[1]*100,pos1[0]*100+Testobject1\Pos[0]*100+force1[0]*200,pos1[1]*100+Testobject1\Pos[1]*100+force1[1]*200
	Line pos2[0]*100+Testobject1\Pos[0]*100,pos2[1]*100+Testobject1\Pos[1]*100,pos2[0]*100+Testobject1\Pos[0]*100+force2[0]*200,pos2[1]*100+Testobject1\Pos[1]*100+force2[1]*200
	
	
	Flip
Until KeyHit(1)
End


;Todo next:
; - GUI - Just Easy - 
; - Kollision - Do next -
;~IDEal Editor Parameters:
;~C#Blitz3D